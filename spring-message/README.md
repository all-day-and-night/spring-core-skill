메시지, 국제화 
================

## 메시지

> 여러 화면에 보이는 상품명, 가격, 수량 등 label에 있는 단어를 변경하려면 다음 화면들을 다 찾아가면서 모두 변경해야 한다.

> 화면이 수십 개 이상이라면 수십 개의 파일을 모두 고쳐야 한다.

> 이런 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 메시지 기능이라 한다.


* messages.properties

> message properties를 생성하고 사용할 message를 정의한다.

```
hello=안녕
hello.name=안녕 {0}

label.item=상품
label.item.id=상품 ID
label.item.itemName=상품명
label.item.price=가격
label.item.quantity=수량

page.items=상품 목록
page.item=상품 상세
page.addItem=상품 등록
page.updateItem=상품 수정
page.insert=상품 입력

button.save=저장
button.cancel=취소

result.complete=저장 완료
```

> application.properties에서 명시적으로 정의해도 되지만 default 값으로 spring.messages.basename=messages 값이 들어온다.

> thymeleaf에서 th:text="#{page.addItem}"으로 tag에 넣어주면 messages.properties에서 정한 값이 들어오게 된다.

> 이러한 방법을 통해 수 많은 html 파일을 수정하지 않아도 되며 국제화를 통해 다른 언어로 쉽게 변환할 수 있다.



## 국제화

> 각 나라별로 별도로 관리

> 다른 messages.properties 파일을 통해 웹 브라우저에 설정된 언어에 따라 바뀔 수 있다.


* messages_en.properties

```
hello=hello
hello.name=hello {0}

label.item=Item
label.item.id=Item ID
label.item.itemName=Item Name
label.item.price=price
label.item.quantity=quantity

page.items=Item List
page.item=Item Detail
page.addItem=Item Add
page.updateItem=Item Update
page.insert=Item Insert

button.save=Save
button.cancel=Cancel

result.complete=save complete
```

* 한글설정(default)

![image](https://user-images.githubusercontent.com/94096054/161958463-3d7e40f3-8056-45a0-89c8-494f42929950.png)


* 영어설정
 
![image](https://user-images.githubusercontent.com/94096054/161958532-7fee137e-851b-49ae-b641-3ff42c906be5.png)

> 이와 같이 페이지를 복사하여 언어를 바꿀 필요 없이 messages의 국제화를 통해 쉽게 각 언어별 렌더링이 가능하다.




