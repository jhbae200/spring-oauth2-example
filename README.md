# Spring OAuth2 Authorization Server
[https://tools.ietf.org/html/rfc6749](https://tools.ietf.org/html/rfc6749) 해당 명세서와 [openid-connect](https://openid.net/specs/openid-connect-core-1_0.html) 스펙서를 참고하여 
스프링으로 구현
# Required
Mysql, Redis, Java 8 이상

# Init Database
[src/test/resource/schema.sql](src/test/resources/schema.sql), [src/test/resource/data.sql](src/test/resources/data.sql)
유저 password 암호화는 BCryptPasswordEncoder로 encode 하였음

# Run Configurations
## Required ENV  
- DB_HOST=
- DB_PORT=
- DB_USERNAME=
- DB_PASSWORD=
- DB_SCHEMA=

default __redis server__ host: __localhost__, port: __6379__

# Endpoints
## GET /.well-known/jwks.json
JSON 웹 토큰 (JWT) 을 확인하는 데 사용되는 공개 키를 포함하는 키 세트입니다.

https://auth0.com/docs/tokens/concepts/jwks

### Reseponse
| 변수명     	| 타입   	| 설명                                                        	|
|------------	|--------	|-------------------------------------------------------------	|
| keys[].alg 	| String 	| 키와 함께 사용되는 특정 암호화 알고리즘입니다.              	|
| keys[].kty 	| String 	| 키와 함께 사용되는 암호화 알고리즘 종류.                    	|
| keys[].use 	| String 	| 키를 어떻게 사용했는지에 대한 정보입니다. sig을 나타냅니다. 	|
| keys[].n   	| String 	| RSA PublicKey 모듈입니다.                                   	|
| keys[].e   	| String 	| RSA PublicKey 비밀 지수(exponent)입니다.                    	|
| keys[].kid 	| String 	| 키의 고유 식별자입니다.                                     	|

## GET /.well-known/openid-configuration
https://openid.net/specs/openid-connect-discovery-1_0.html

## GET /oauth2/v1/auth
client id redirect uri 체크후 로그인페이지 노출(/templates/oauth2/login.html)

### Parameters
| 변수명                	| 구분  	| 타입    	| 필수 	| 설명                                                                                                                                                                                                                                  	|
|-----------------------	|-------	|---------	|------	|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| client_id             	| Param 	| String  	| O    	| Client Id                                                                                                                                                                                                                             	|
| code_challenge        	| Param 	| String  	| X    	| native app에서 인증시 보안을 위해 사용(RFC7636) A-Z, a-z, 0-9, -._~로 이루어진 예측할 수 없는 랜덤 문자열 이여야하며 48~128 이내. code_challenge_method가 sha256인 경우 BASE64URL-ENCODE(SHA256(ASCII(code_verifier))) 	|
| code_challenge_method 	| Param 	| String  	| X    	| S256 or plain default: plain                                                                                                                                                                                                          	|
| nonce                 	| Param 	| String  	| X    	| 클라이언트 세션을 ID 토큰과 연결하고 재생 공격을 완화하는 데 사용되는 문자열 값입니다. 이 값은 인증 요청에서 ID 토큰으로 수정되지 않은 상태로 전달됩니다.                                                                             	|
| redirect_uri          	| Param 	| String  	| O    	| 성공후 redirect될 uri                                                                                                                                                                                                                 	|
| response_type         	| Param 	| String  	| O    	| code                                                                                                                                                                                                                                  	|
| state                 	| Param 	| String  	| X    	| 클라이언트가 요청과 콜백 사이의 상태를 유지하기 위해 사용하는 불투명 한 값(cross-site request) 해당 state값은 redirect_uri에 포함되어 redirect되며 클라이언트가 보낸 state값과 같은지 확인해야 함                                     	|

## POST /oauth2/v1/token?grant_type=authorization_code

코드를 사용하여 권한을 부여하는 액세스 토큰과 새로고침 토큰을 얻습니다.

### Parameters
| 변수명        	| 구분  	| 타입   	| 필수 	| 설명                                                                                                                                                                                              	|
|---------------	|-------	|--------	|------	|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| grant_type    	| Body  	| String 	| O    	| authorization_code                                                                                                                                                                                	|
| client_id     	| Body  	| String 	| O    	| Client Id                                                                                                                                                                                         	|
| client_secret 	| Body  	| String 	| X    	| Client Secret (없으면 code_verifier검증해야함)                                                                                                                                                    	|
| code          	| Body  	| String 	| O    	| Authorization Endpoint에서 받은 권한 코드입니다.                                                                                                                                                  	|
| code_verifier 	| Body  	| String 	| X    	| Authorization Endpoint에서 보낸 code_challenge와 일치하는지 확인하는 검증 코드                                                                                                                    	|
| state         	| Param 	| String 	| X    	| 클라이언트가 요청과 콜백 사이의 상태를 유지하기 위해 사용하는 불투명 한 값(cross-site request) 해당 state값은 redirect_uri에 포함되어 redirect되며 클라이언트가 보낸 state값과 같은지 확인해야 함 	|

### Response
| 변수명        	| 타입   	| 설명                                                                                                                            	|
|---------------	|--------	|---------------------------------------------------------------------------------------------------------------------------------	|
| access_token  	| String 	| 권한 서버가 발행 한 액세스 토큰                                                                                                 	|
| expires_in    	| long   	| 액세스 토큰의 수명 (초)                                                                                                         	|
| id_token      	| String 	| 2. Id Token OpenID Connect가 OAuth 2.0에서 최종 사용자를 인증 할 수 있도록 하는 id token                                        	|
| refresh_token 	| String 	| Section 6에서 설명한 것과 동일한 권한 부여를 사용하여 새 액세스 토큰을 얻는 데 사용할 수있는 새로 고침 토큰 만료 시간 현재 없음 	|
| token_type    	| String 	| Section 7.1에서 설명한대로 발행 된 토큰의 유형 값, 값은 대소 문자를 구분하지 않습니다.                                          	|

## POST /oauth2/v1/token?grant_type=password

username과 password를 사용하여 권한을 부여하는 액세스 토큰과 새로고침 토큰을 얻습니다.

### Parameters
| 변수명        	| 구분  	| 타입   	| 필수 	| 설명                                                                                                                                                                                              	|
|---------------	|-------	|--------	|------	|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| grant_type    	| Body  	| String 	| O    	| authorization_code                                                                                                                                                                                	|
| client_id     	| Body  	| String 	| O    	| Client Id                                                                                                                                                                                         	|
| client_secret 	| Body  	| String 	| O    	| Client Secret (없으면 code_verifier검증해야함)                                                                                                                                                    	|
| password         	| Body  	| String 	| O    	| 유저 비밀번호                                                                                                                                                  	|
| username      	| Body  	| String 	| O    	| 유저 이메일                                                                                                                    	|

### Response
| 변수명        	| 타입   	| 설명                                                                                                                            	|
|---------------	|--------	|---------------------------------------------------------------------------------------------------------------------------------	|
| access_token  	| String 	| 권한 서버가 발행 한 액세스 토큰                                                                                                 	|
| expires_in    	| long   	| 액세스 토큰의 수명 (초)                                                                                                         	|
| id_token      	| String 	| 2. Id Token OpenID Connect가 OAuth 2.0에서 최종 사용자를 인증 할 수 있도록 하는 id token                                        	|
| refresh_token 	| String 	| Section 6에서 설명한 것과 동일한 권한 부여를 사용하여 새 액세스 토큰을 얻는 데 사용할 수있는 새로 고침 토큰 만료 시간 현재 없음 	|
| token_type    	| String 	| Section 7.1에서 설명한대로 발행 된 토큰의 유형 값, 값은 대소 문자를 구분하지 않습니다.                                          	|

## POST /oauth2/v1/token?grant_type=refresh_token

새로 고침 토큰을 제공하여 액세스 토큰을 얻습니다.

### Parameters
| 변수명        	| 구분  	| 타입   	| 필수 	| 설명                                                                                                                                                                                              	|
|---------------	|-------	|--------	|------	|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| grant_type    	| Body  	| String 	| O    	| authorization_code                                                                                                                                                                                	|
| client_id     	| Body  	| String 	| O    	| Client Id                                                                                                                                                                                         	|
| refresh_token    	| Body  	| String 	| O    	| 새로고침 토큰                                                                                                                                                  	|

### Response
| 변수명        	| 타입   	| 설명                                                                                                                            	|
|---------------	|--------	|---------------------------------------------------------------------------------------------------------------------------------	|
| access_token  	| String 	| 권한 서버가 발행 한 액세스 토큰                                                                                                 	|
| expires_in    	| long   	| 액세스 토큰의 수명 (초)                                                                                                         	|
| id_token      	| String 	| 2. Id Token OpenID Connect가 OAuth 2.0에서 최종 사용자를 인증 할 수 있도록 하는 id token                                        	|
| refresh_token 	| String 	| Section 6에서 설명한 것과 동일한 권한 부여를 사용하여 새 액세스 토큰을 얻는 데 사용할 수있는 새로 고침 토큰 만료 시간 현재 없음 	|
| token_type    	| String 	| Section 7.1에서 설명한대로 발행 된 토큰의 유형 값, 값은 대소 문자를 구분하지 않습니다.                                          	|

## GET /oauth2/v1/userinfo
토큰이 유효한지 확인 후 해당 토큰의 정보를 리턴합니다.

### Parameters
| 변수명        	| 구분   	| 타입   	| 필수 	| 설명                    	|
|---------------	|--------	|--------	|------	|-------------------------	|
| Authorization 	| Header 	| String 	| O    	| Bearer {{access_token}} 	|

### Response
JWT Body (json)

# TODO 

- accessToken revoke 방법
- Social Login
- User Signup
