package com.ajou_nice.with_pet.service;


import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.exception.AppException;
import com.ajou_nice.with_pet.exception.ErrorCode;
import com.ajou_nice.with_pet.repository.UserRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

	@Value("${cloud.aws.S3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	//여러개가 들어와야 한다. (house image같은 경우는 여러장을 한꺼번에 넘기기 때문에)
	//house일때 어떻게 구분해주냐가 관건인거같다 ..젠장 (아니면 controller에서 요청자체가 다르게
	//할 것인가도 생각해봐야겠음l
	public List<String> uploadFile(List<MultipartFile> multipartFiles){

		List<String> fileNameList = new ArrayList<>();
		// forEach 구문을 통해 multipartFiles 리스트로 넘어온 파일들을 순차적으로 fileNameList 에 추가
		multipartFiles.forEach(file -> {
			//원래 기존의 file이름과 랜덤 uuid를 통해서 파일명 생성해줌
			//s3에는 같은 파일의 이름이 들어갈 수 없기 때문에
			String fileName = createFileName(file.getOriginalFilename());
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentLength(file.getSize());
			objectMetadata.setContentType(file.getContentType());

			//s3 bucket에 업로드 시켜주는 코드
			try(InputStream inputStream = file.getInputStream()){
				amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
						.withCannedAcl(CannedAccessControlList.PublicRead));
			} catch (IOException e){
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
			}

			String uploadUrl = "https://withpet.s3.ap-northeast-2.amazonaws.com/" + fileName;
			fileNameList.add(uploadUrl);

		});

		//fileNameList는 S3에 업로드된 사진 img url list들
		return fileNameList;
	}

	// 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
	public String createFileName(String fileName){
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}

	// file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단
	private String getFileExtension(String fileName){
		try{
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
		}
	}

	public void deleteFile(String fileName){
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
		System.out.println(bucket);
	}


}
