# Built-in Node

## 실행

```sh
docker-compose up -d
docker-compose logs -f
```

## Access Control

- [Managing Security](https://www.jenkins.io/doc/book/security/managing-security/)
  - [Role-based Authorization Strategy](https://plugins.jenkins.io/role-strategy/)
  - [Matrix Authorization Strategy](https://plugins.jenkins.io/matrix-auth/)

> Manage Jenkins > Configure Global Security

- Authentication
  - Security Realm: `Jenkins' own user database`

> Manage Jenkins > Manage and Assign Roles

- Manage Roles
  - Role 추가
- Assign Roles
  - User 혹은 Group 추가 후 role 할당
  - Group은 [LDAP](https://plugins.jenkins.io/ldap/)이 필요하다.

## 정리

```sh
docker-compose down
```
