### Access Token 재발급
method|path|description
---|---|---
GET|/auth/refresh|쿠키에 저장된 refresh token을 사용하여 access token을 재발급한다.

request
- header :
    ```json
    Content-type: "application/json"
    ```
    
response
- data :
    ```json
    {
        token: string
    }
    ```

error
- data :
    ```json
    {
        statusCode: number,
        status: string,
        message: string
    }
    ```

    statusCode|Description
    :-:|:--
    401|access token이나 refresh token 유효성 검사를 통과하지 못한 경우

### 로그아웃
method|path|description
---|---|---
POST|/auth/logout|DB에 저장되어 있는 refresh token을 삭제한다.

request
- header :
    ```json
    Content-type: "application/json"
    Authorization: {accessToken}
    ```

response
- data : null

error
- data :
    ```json
    {
        statusCode: number,
        status: string,
        message: string
    }
    ```

    statusCode|Description
    :-:|:--
    403|access token이 유효성 검사를 통과하지 못한 경우