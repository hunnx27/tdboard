# 브랜치 컨벤션
## 기본 규칙
- 작업 전 master와 작업브랜치 pull 받을 것
- 특별한 경우가 아니라면 작업브랜치로 checkout한 후 master브랜치와 merge하고 작업 시작

## master
운영 배포용 브랜치

## feats/*
기능 추가시 브랜치
- feats/dev_pub :  퍼블리싱 적용
- feats/dev_security : 스프링 시큐리티 적용(인증, 보안)

# Security Multiple 구성 참고
https://github.com/dedel009/securityLogin/blob/master/src/main/java/com/exam/configure/UserSecurityConfiguration.java

# URL 컨벤션
- /{name}      : 페이지이동
- /{name}_proc : 프로세스 구현(form, ajax)