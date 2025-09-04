package com.petrov.core_crm.job;

import com.petrov.core_crm.entity.Building;
import com.petrov.core_crm.entity.Task;
import com.petrov.core_crm.entity.User;
import com.petrov.core_crm.enums.TaskPriority;
import com.petrov.core_crm.enums.TaskStatus;
import com.petrov.core_crm.repository.BuildingRepository;
import com.petrov.core_crm.repository.TaskRepository;
import com.petrov.core_crm.repository.UserRepository;
import com.petrov.core_crm.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InactiveBuildingsJob extends QuartzJobBean {

	private final BuildingRepository buildingRepository;
	private final TaskRepository taskRepository;
	private final UserRepository userRepository;
	private final NotificationService notificationService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);
		List<Building> inactiveBuildings = buildingRepository.findBuildingsWithNoRecentActivity(thresholdDate);
		List<User> managers = userRepository.findAll();

		log.info("Found {} inactive buildings", inactiveBuildings.size());

		for (Building building : inactiveBuildings) {
			Task task = new Task();
			task.setTitle("Проверить активность объекта: " + building.getCadastrNumber());
			task.setDescription("Объект недвижимости не обновлялся более 30 дней. Свяжитесь с владельцем.");
			task.setDueDate(LocalDateTime.now().plusDays(7));
			task.setStatus(TaskStatus.PENDING);
			task.setPriority(TaskPriority.MEDIUM);
			task.setBuilding(building);

			if (!managers.isEmpty()) {
				User manager = managers.getFirst();
				task.setAssignedTo(manager);

				Task savedTask = taskRepository.save(task);

				try {
					notificationService.sendAutoTaskCreatedNotification(savedTask);
				} catch (Exception e) {
					log.error("Failed to send notification for auto-created task: {}", savedTask.getId(), e);
				}
			}
		}
	}
}
