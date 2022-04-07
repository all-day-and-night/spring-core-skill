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

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
            errors.put("price", "가격은 1000 ~ 1000000까지 허용합니다.");
        }

        if(item.getQuantity() == null || item.getQuantity() > 0 || item.getQuantity() >= 9999){
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



