![위드펫](https://github.com/ajousw-withpet/with_pet_backend/assets/103014749/c479bd00-60b6-4e1e-8c8f-ea0bade9edd0)
### 프로젝트 소개

> ‘위드펫’은 반려인과 펫시터를 중개하는 플랫폼입니다.
펫시터는 반려견의 사회화 온도(사람 혹은 다른 반려견과의 사회성을 나타내는 척도)와 반려인의 애정도(반려견에 대한 관심도)를 확인하고 수락 및 거부하며 서비스를 유동적으로 관리할 수 있습니다.
> 

### About 위드펫

1. 그룹 일지 작성 및 조회 기능
    - 반려인은 그룹에 소속된 반려견에 대한 일지를 작성 및 조회할 수 있습니다.
2. 원하는 조건의 펫시터 예약 기능
    - 반려인은 원하는 조건의 펫시터를 필터링하여 찾을 수 있으며 돌봄 서비스를 예약할 수 있습니다.
3. 온도 시스템 기능
    - 펫시터는 온도 정보를 조회하여 반려견의 사회성 정도와 반려인의 반려견 관심 정도를 파악한 후 예약을 승낙 및 거절할 수 있습니다.

### ajouNice 백엔드

| name | part | email | github |
| --- | --- | --- | --- |
| 장승현 | `펫시터 API`<br>`관리자 API`<br>`펫시터 지원 및 관리 API`<br>`예약 API`<br>`예약 관리 기능`<br>`결제 API`<br>`채팅 기능`<br>`S3 연동`<br>`API 설계 및 DB설계` | mailto:jason5102@ajou.ac.kr | https://github.com/j-seunghyun |
| 김지수 | `로그인, 회원가입 API`<br>`회원 관리 API`<br>`일지 API`<br>`알림 API`<br>`예약 API`<br>`swagger 연동`<br>`시큐리티 연동`<br>`CI/CD 관리`<br>`API 설계 및 DB설계` | mailto:chensa1018@ajou.ac.kr | https://github.com/strongcookdas |

### 사용 기술 스택 소개

- ajouNice팀의 백엔드 API는 Spring Boot를 활용하여 개발을 진행하였고, Swagger를 사용하여 API 문서를 생성했습니다.
- Spring Boot Security을 통해 로그인 시스템을 구축했습니다.
- 데이터베이스는 RDBMS인 MySQL을 사용했습니다.
- AWS EC2와 GitHub Actions를 통해 CICD를 구축하여 배포 자동화 시스템을 구축했습니다.
- API 명세 : [https://withpet.site/swagger-ui/](https://withpet.site/swagger-ui/)

## 🛠️ skill
<h3>Language</h3> 
<div>
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> &nbsp;
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> &nbsp;
  <img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> 
</div>
<h3>Database</h3>
<div>
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
</div>
<h3>AWS</h3>
<div>
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">&nbsp;
  <img src="https://img.shields.io/badge/s3-569A31?style=for-the-badge&logo=s3&logoColor=white">
</div>
<h3>Management</h3>
<div>
  <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> &nbsp;
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> &nbsp;
  <img src="https://img.shields.io/badge/githubaction-2088ff?style=for-the-badge&logo=githubaction&logoColor=white">
</div>

### 실행방법

- application-aws.yml 파일 생성 후 아래 내용 기입
    
    ```yaml
    spring:
      mvc:
        pathmatch:
          matching-strategy: ant_path_matcher
      datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: #데이터베이스 url
        username: #데이터베이스 username
        password: #데이터베이스 패스워드
      jpa:
        database: mysql
        show-sql: true
        hibernate:
          ddl-auto: update
        properties:
          hibernate:
            show_sql: true
            format_sql: true
    
    ajou:
      nice: #value
    
    jwt:
      token:
        secret: #value
    
    cloud:
      aws:
        credentials:
          access-key: #aws access-key
          secret-key: #aws secret-key
        S3:
          bucket: #S3 bucket
        region:
          static: #resion static
        stack:
          auto: false
    logging:
      level:
        com:
          amazonaws:
            util:
              EC2MetadataUtils: error
    ```
    
- 실행 후 [http://localhost:8080/swagger-ui/#](http://localhost:8080/swagger-ui/#/user-controller/joinUsingPOST) 접속

### 패키지 구조

- configuration - 설정 클래스 관리
- controller - Controller 클래스 관리
- domain - 엔티티 클래스, dto 클래스 관리
- enums - enum 클래스 관리
- exception - exception 문구 클래스, 핸들러 클래스 관리
- repository - repository 클래스 관리
- service - service 클래스 관리
- uitls - 전역 함수 선언 클래스 관리

### 개발환경

- java11
- Spring Boot 2.7.11
- Build Tool - Gradle
