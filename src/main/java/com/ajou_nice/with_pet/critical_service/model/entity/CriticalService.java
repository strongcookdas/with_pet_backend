package com.ajou_nice.with_pet.critical_service.model.entity;

import com.ajou_nice.with_pet.admin.model.dto.add_critical.AddCriticalServiceRequest;
import com.ajou_nice.with_pet.admin.model.dto.update_critical.UpdateCriticalServiceRequest;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@Table(name="critical_withpetservice")
public class CriticalService {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "critical_serviceId")
	private Long id;

	private String introduction;

	private String serviceName;

	@Lob
	private String serviceImg;

	public void updateServiceInfo(UpdateCriticalServiceRequest updateCriticalServiceRequest){
		this.serviceName = updateCriticalServiceRequest.getServiceName();
		this.serviceImg = updateCriticalServiceRequest.getServiceImg();
		this.introduction = updateCriticalServiceRequest.getServiceIntroduction();
	}
	public static CriticalService forSimpleTest(String introduction, String serviceName, String serviceImg){
		return CriticalService.builder()
				.serviceName(serviceName)
				.serviceImg(serviceImg)
				.introduction(introduction)
				.build();
	}

	public static CriticalService toEntity(AddCriticalServiceRequest addCriticalServiceRequest){
		String img = addCriticalServiceRequest.getServiceImg();

		if(addCriticalServiceRequest.getServiceImg() == null || addCriticalServiceRequest.getServiceImg().isEmpty()){
			img = "data:image/png;base64,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";
		}
		return CriticalService.builder()
				.serviceName(addCriticalServiceRequest.getServiceName())
				.serviceImg(img)
				.introduction(addCriticalServiceRequest.getServiceIntro())
				.build();
	}
}
