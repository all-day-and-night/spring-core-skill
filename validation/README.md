Validation
===============

> 검증 요구사항이 있거나 검증 오류가 발생하면 오류화면으로 이동한다.

> 이렇게 될 경우 사용자는 처음부터 해당 폼으로 다시 이동해서 입력해야 한다.

> 웹 서비스는 폼 입력시 오류가 발생하면, 고객이 입력한 데이터를 유지한 상태로 어떤 오류가 발생했는지 전달해야 한다.


* 참고:  클라이언트 검증, 서버 검증

  + 클라이언트 검증은 조작할 수 있으므로 보안에 취약하다.
  + 서버만으로 검증하면, 즉각적인 고객 사용성이 부족해진다.
  + 둘을 적절히 섞어서 사용하되, 최종적으로 서버 검증은 필수
  + API 방식을 사용하면 API 스펙을 잘 정의해서 검증 오류를 API 응답 결과에 잘 남겨 주어야 함

* 정리

> 검증 오류가 발생하면 입력 폼을 다시 보여준다
> 검증 오류들을 고객에게 안내하여 다시 입력한다.
> 검증 오류 발생시 입력한 데이터 유지

## V1

```
@PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        //검증오류 결과를 보관
        Map<String, String> errors = new HashMap<>();

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            errors.put("itemName", "상품 이름은 필수 입니다.");
        }

        if(item.getPrice() == null || item.getPrice() >= 1000 || item.getPrice() <= 1000000){
            errors.put("price", "가격은 1000 ~ 1000000까지 허용합니다.");
        }

        if(item.getQuantity() == null || item.getQuantity() > 0 || item.getQuantity() <= 9999){
            errors.put("quantity", "수량은 최대 9999까지 허용합니다.");
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null || item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                errors.put("globalError", "가격 * 수량의 합은 10000원 이상이어야 합니다. 현재 값 = " + resultPrice);
            }
        }

        // 검증에 실패하면 다시 입력폼으로
        if(!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }
```

> 하지만 int 타입의 변수에 문자열을 넣을 경우 오류가 발생한다. 

> 해결할 방법을 찾아보자


## V2

* bindingResult

> PostMapping을 하는 controller에서 @ModelAttribute로 받는 DTO뒤에 BindingResult를 매개변수로 받는다.

```
//field에 값이 존재할 경우
bindingResult.addError(new FieldError(objectName, field, defaultMessage));

//filed에 값이 존재하지 않고 새로 생성할 경우
bindingResult.addError(new ObjectError(objectName, defaultMessage));
```

> 이러한 bindResult를 반환하여 thymeleaf로 view파일에 넘겨주고 errorclass 등을 활용하여 예외 처리를 보여준다.


* @ModelAttribute에 바인딩 시 타입 오류가 발생하면?

> BindingResult가 없으면 400오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동

> BindingResult가 있으면 오류 정보(fieldError나 ObjectError를 BindingResult에 담아서 컨트롤러에 정상호출한다.)

> 하지만 값이 저장되어 돌아오지 않았다.

> 추가적인 해결 방법으로 문제를 해결해보자.


## FieldError, ObjectError

### FiledError

> FiledError는 두 가지 생성자를 제공한다.

```
public FieldError(String objectName, String field, String defaultMessage);
public FieldError(String objectName, String field, @Nullable Object 
rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable
Object[] arguments, @Nullable String defaultMessage)
```
* Parameter
  + objectName : 오류가 발생한 객체 이름
  + field : 오류 필드
  + rejectedValue : 사용자가 입력한 값(거절된 값)
  + bindingFailure : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
  + codes : 메시지 코드
  + arguments : 메시지에서 사용하는 인자
  + defaultMessage : 기본 오류 메시지


### ObjectError

> ObjectError 역시 두 가지 생성자를 제공한다.

```
public ObjectError(String objectName, @Nullable String defaultMessage);
public ObjectError(String objectName, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage);
```

> FieldError와 다르게 값을 저장할 필요가 없기 때문에 parameter가 적다.

* 타임리프 스프링 검증 오류 통합 기능

> 타임리프는 스프링의 BindingResult 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.
  + #fields : #fields 로 BindingResult 가 제공하는 검증 오류에 접근할 수 있다.
  + th:errors : 해당 필드에 오류가 있는 경우에 태그를 출력한다. th:if 의 편의 버전이다.
  + th:errorclass : th:field 에서 지정한 필드에 오류가 있으면 class 정보를 추가한다.

## V2-2

> FieldError와 ObjectError의 또 다른 생성자를 사용하여 예외처리와 오류난 값을 저장하여 돌려주는 것을 해결했다.

```
@PostMapping("/add")
    public String addItemv2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, null, null, "상품 이름은 필수 입니다."));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            //bindingResult.addError(new FieldError("item", "price", "가격은 1000~1000000까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, null, null, "가격은 1000~1000000까지 허용합니다"));
        }

        if(item.getQuantity() == null || item.getQuantity() <= 0 || item.getQuantity() > 9999){
            //bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9999까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, null, null, "수량은 최대 9999까지 허용합니다."));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null || item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", null,null, "가격 * 수량의 합은 10000원 이상이어야 합니다. 현재 값 = " + resultPrice));
            }
        }

        // 검증에 실패하면 다시 입력폼으로
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            // 모델에 굳이 담지 않아도 스프링에서 자동으로 모델에 담아 전달한다.
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```


## V3

> 오류난 값을 돌려줬지만 messages를 사용하여 오류 메시지를 전역으로 사용할 수 있게 구현하였다.


* code

```
@PostMapping("/add")
    public String addItemv3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        //검증 로직
        if(!StringUtils.hasText(item.getItemName())){
            //bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(), false, new String[]{"required.item.itemName"}, null, null));
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            //bindingResult.addError(new FieldError("item", "price", "가격은 1000~1000000까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if(item.getQuantity() == null || item.getQuantity() <= 0 || item.getQuantity() > 9999){
            //bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9999까지 허용합니다."));
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 특정 필드가 아닌 복합 룰 검증
        if(item.getPrice() != null || item.getQuantity() != null){
            int resultPrice = item.getPrice() * item.getQuantity();
            if(resultPrice < 10000){
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},new Object[]{10000, resultPrice}, null));
            }
        }

        // 검증에 실패하면 다시 입력폼으로
        if(bindingResult.hasErrors()){
            //model.addAttribute("errors", errors);
            // 모델에 굳이 담지 않아도 스프링에서 자동으로 모델에 담아 전달한다.
            return "validation/v2/addForm";
        }

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
```

> filedError, ObjectError에 code와 args를 사용하여 error 메시지를 가져와 사용한다.

* Error.properties

```
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.
totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
```



