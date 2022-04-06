thymeleaf - 기본기능
===================

## 프로젝트 생성

* Project: Gradle Project
* Language: Java
* Spring Boot: 2.6.6
* Project Metadata
* Group: hello
* Artifact: thymeleaf-basic
* Name: thymeleaf-basic
* Package name: hello.thymeleaf
* Packaging: Jar
* Java: 11

## 타임리프 특징

* 서버 사이드 HTML 렌더링
* 네츄럴 템플릿
* 스프링 통합 지원


## Main page

![image](https://user-images.githubusercontent.com/94096054/161893826-7d87eb91-4e0c-4503-8ffe-64c32b54af9a.png)


* main reference

```
<body>
<ul>
    <li>텍스트
        <ul>
            <li><a href="/basic/text-basic">텍스트 출력 기본</a></li>
            <li><a href="/basic/text-unescaped">텍스트 text, utext</a></li>
        </ul>
    </li>
    <li>표준 표현식 구문
        <ul>
            <li><a href="/basic/variable">변수 - SpringEL</a></li>
            <li><a href="/basic/basic-objects?paramData=HelloParam">기본 객체들</a></li>
            <li><a href="/basic/date">유틸리티 객체와 날짜</a></li>
            <li><a href="/basic/link">링크 URL</a></li>
            <li><a href="/basic/literal">리터럴</a></li>
            <li><a href="/basic/operation">연산</a></li>
        </ul>
    </li>
    <li>속성 값 설정
        <ul>
            <li><a href="/basic/attribute">속성 값 설정</a></li>
        </ul>
    </li>
    <li>반복
        <ul>
            <li><a href="/basic/each">반복</a></li>
        </ul>
    </li>
    <li>조건부 평가
        <ul>
            <li><a href="/basic/condition">조건부 평가</a></li>
        </ul>
    </li>
    <li>주석 및 블록
        <ul>
            <li><a href="/basic/comments">주석</a></li>
            <li><a href="/basic/block">블록</a></li>
        </ul>
    </li>
    <li>자바스크립트 인라인
        <ul>
            <li><a href="/basic/javascript">자바스크립트 인라인</a></li>
        </ul>
    </li>
    <li>템플릿 레이아웃
        <ul>
            <li><a href="/template/fragment">템플릿 조각</a></li>
            <li><a href="/template/layout">유연한 레이아웃</a></li>
            <li><a href="/template/layoutExtend">레이아웃 상속</a></li>
        </ul>
    </li>
</ul>
</body>
</html>

```


## 기본 표현식

* 간단한 표현:
  
  + 변수 표현식: ${...}
  + 선택 변수 표현식: *{...}
  + 메시지 표현식: #{...}
  + 링크 URL 표현식: @{...}
  + 조각 표현식: ~{...}

* 리터럴
  
  + 텍스트: 'one text', 'Another one!',…
  + 숫자: 0, 34, 3.0, 12.3,…
  + 불린: true, false
  + 널: null
  + 리터럴 토큰: one, sometext, main,…

* 문자 연산:
  
  + 문자 합치기: +
  + 리터럴 대체: |The name is ${name}|

* 산술 연산:
  
  + Binary operators: +, -, *, /, %
  + Minus sign (unary operator): -

* 불린 연산:
  
  + Binary operators: and, or
  + Boolean negation (unary operator): !, not

* 비교와 동등:
  
  + 비교: >, <, >=, <= (gt, lt, ge, le)
  + 동등 연산: ==, != (eq, ne)

* 조건 연산:
  
  + If-then: (if) ? (then)
  + If-then-else: (if) ? (then) : (else)
  + Default: (value) ?: (defaultvalue)

* 특별한 토큰:
  
  + No-Operation: _


## 기본 객체

* ${#request}
* ${#response}
* ${#session}
* ${#servletContext}
* ${#locale}

* HTTP request param 접근

  + ${param.paramData}
  + ${session.sessionData}
  + ${@helloBean.hello('Spring!')}

