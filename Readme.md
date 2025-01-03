# 기능 명세
![01 요구사항](https://github.com/user-attachments/assets/8c313c9c-5aab-47b3-a502-3a7c6f72e189)
- 회원 기능
  - 회원 등록
  - 회원 조회
- 상품 기능
  - 상품 등록
  - 상품 수정
  - 상품 조회
- 주문 기능
  - 상품 주문
  - 주문 내역 조회
  - 주문 취소
- 기타 요구사항
  - 상품은 제고 관리가 필요하다
  - 상품의 종류는 도서, 음반, 영화가 있다
  - 상품을 카테고리로 구분할 수 있다
  - 상품 주문시 배송 정보를 입력할 수 있다

# 도메인 모델과 테이블 설계
## 도메인 모델
![02 도메인](https://github.com/user-attachments/assets/f82c94ae-811f-4d97-a8a9-9b4720efc500)
- 회원 - 주문 = 1:N
- 주문 - 배송 = 1:1
- 주문 - 주문상품 = N:N
- 주문상품 - 상품 = N:1
- 상품 - 카테고리 = N:N

## 엔티티 분석
![03 엔티티](https://github.com/user-attachments/assets/0e661122-6853-49fa-b5a8-6b6904b62ef6)
- 회원(Member): 이름과 임베디드 타입인 주소( Address ), 그리고 주문( orders ) 리스트를 가진다.
- 주문(Order): 한 번 주문시 여러 상품을 주문할 수 있으므로 주문과 주문상품( OrderItem )은 일대다 관계다. 주문은
  상품을 주문한 회원과 배송 정보, 주문 날짜, 주문 상태( status )를 가지고 있다. 주문 상태는 열거형을 사용했는데 주
  문( ORDER ), 취소( CANCEL )을 표현할 수 있다.
- 주문상품(OrderItem): 주문한 상품 정보와 주문 금액( orderPrice ), 주문 수량( count ) 정보를 가지고 있다. (보
  통 OrderLine , LineItem 으로 많이 표현한다.)
- 상품(Item): 이름, 가격, 재고수량( stockQuantity )을 가지고 있다. 상품을 주문하면 재고수량이 줄어든다. 상품의
  종류로는 도서, 음반, 영화가 있는데 각각은 사용하는 속성이 조금씩 다르다.
- 배송(Delivery): 주문시 하나의 배송 정보를 생성한다. 주문과 배송은 일대일 관계다.
- 카테고리(Category): 상품과 다대다 관계를 맺는다. parent , child 로 부모, 자식 카테고리를 연결한다.
- 주소(Address): 값 타입(임베디드 타입)이다. 회원과 배송(Delivery)에서 사용한다.

> 참고: 회원이 주문을 하기 때문에, 회원이 주문리스트를 가지는 것은 얼핏 보면 잘 설계한 것 같지만, 객체 세상은
실제 세계와는 다르다. 실무에서는 회원이 주문을 참조하지 않고, 주문이 회원을 참조하는 것으로 충분하다. 여기
서는 일대다, 다대일의 양방향 연관관계를 설명하기 위해서 추가했다.

## 테이블 설계
![04 테이블](https://github.com/user-attachments/assets/e5214cbb-2138-4b91-8ed6-6548e58e2b3e)
- MEMBER: 회원 엔티티의 Address 임베디드 타입 정보가 회원 테이블에 그대로 들어갔다. 이것은 DELIVERY 테
이블도 마찬가지다.
- ITEM: 앨범, 도서, 영화 타입을 통합해서 하나의 테이블로 만들었다. DTYPE 컬럼으로 타입을 구분한다.
- CATEGORY_ITEM: 관계형 데이터 베이스에서는 다대다를 표현할 수 없기 때문에 이와 같이 매핑 테이블을 둬야 한다.

> 참고: 테이블명이 ORDER 가 아니라 ORDERS 인 것은 데이터베이스가 order by 때문에 예약어로 잡고 있는
경우가 많다. 그래서 관례상 ORDERS 를 많이 사용한다.

> 참고: 실제 코드에서는 DB에 소문자 + _(언더스코어) 스타일을 사용하겠다.
데이터베이스 테이블명, 컬럼명에 대한 관례는 회사마다 다르다. 보통은 대문자 + _(언더스코어)나 소문자 + _(언
더스코어) 방식 중에 하나를 지정해서 일관성 있게 사용한다. 강의에서 설명할 때는 객체와 차이를 나타내기 위해
데이터베이스 테이블, 컬럼명은 대문자를 사용했지만, 실제 코드에서는 소문자 + _(언더스코어) 스타일을 사용하
겠다.