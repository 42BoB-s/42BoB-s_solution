# 42BoB-s_solution

**(1) Dao vs Respository**
1-1. Repository에서 SQL 문 FROM 테이블에 따라서 SQL문 분리하여 JdbcRoomMatchDao로 편입시킴.
1-2. ActivityLog 인터페이스와 구현체를 만들어서 SQL문 분리하여 편입시킴.
1-3. ActivityLog Insert하는 방식이 기존에 파라미터 넘겨주던 방식에서 Dto를 넘기는 방식으로 변경
1-4. 추가된 메서드는 모두 RoomMatchDao 인터페이스에 추가됨.
1-5. 변경사항에 대응하여 TestCode 변경하였음.**(2) Dto vs Domain**
조사해보니 둘은 서로 다른 디자인 패턴을 의미하는것임. 따라서 한쪽으로 편입시키지 않음. (유지)**(3) 이슈**
3-1. **deleteRoomMatch()** 메서드 : 2개의 서로다른 테이블을 조회하고 있음, 분리가 필요하지 않을까?
3-2. JdbcRoomMatchDao의 **roomInfoRowMapper** : JdbcRoomInfoDao에 완전히 동일한 코드가 있음.
3-3. SQL문 위치 : 도히님은 DAO는 SQL문을 클래스 상위에 변수로 따로 설정하여서 사용하고 있고 엠탁님은 메서드 내부에서 작성해주고 있음, 스타일 통일이 필요함.