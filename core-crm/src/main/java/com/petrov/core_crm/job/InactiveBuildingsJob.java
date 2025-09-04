package com.petrov.core_crm.job;

import com.petrov.core_crm.entity.Building;
import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.enums.TaskPriority;
import com.petrov.core_crm.enums.TaskStatus;
import com.petrov.core_crm.enums.UserRole;
import com.petrov.core_crm.repository.BuildingRepository;
import com.petrov.core_crm.repository.TaskRepository;
import com.petrov.core_crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InactiveBuildingsJob extends QuartzJobBean {

	private final BuildingRepository buildingRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);
		List<Building> inactiveBuildings = buildingRepository.findBuildingsWithNoRecentActivity(thresholdDate);

		List<User> managers = userRepository.findUsersByRole(UserRole.MANAGER);

		for (Building building : inactiveBuildings) {
			Task task = new Task();
			task.setTitle("Проверить активность объекта: " + building.getCadastrNumber());
			task.setDescription("Объект недвижимости не обновлялся более 30 дней!");
			task.setDueDate(LocalDateTime.now().plusDays(7));
			task.setStatus(TaskStatus.PENDING);
			task.setPriority(TaskPriority.MEDIUM);
			task.setBuilding(building);

			if (!managers.isEmpty()) {
				task.setAssignedTo(managers.getFirst());
			}

			taskRepository.save(task);

			building.updateTimestamps();
			buildingRepository.save(building);
		}

	}
}
