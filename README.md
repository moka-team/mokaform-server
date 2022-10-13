# mokaform-server

## Convention
### commit message rule
|Tag|Contents|remarks|
|---|---|---|
|`feat`|새로운 기능 추가||
|`fix`|버그 수정||
|`hotfix`|치명적인 버그 수정|PR merge 규칙 무시 가능|
|`docs`|문서 및 주석 수정||
|`refactor`|프로덕션 코드 리팩토링||
|`chore`|설정 파일 수정|프로덕션 코드에 변경이 없는 경우|
|`test`|테스트 코드 추가, 테스트 리팩토링|프로덕션 코드에 변경이 없는 경우|
|`style`|오타 수정, 코드 포맷팅|프로덕션 코드에 변경이 없는 경우|
|`design`|css 등 사용자 ui 변경||
- `[tag]: [contents] ([Jira ticket number])`
- 예시: `feat: 로그인 페이지 추가 (MOKA-xxxx)`

### branch rule
- `[tag]/[jira ticker number]`
- 예시: `feat/MOKA-22`
