# Project Methods

## Security

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/security/auth
</summary>
<p>Description: Получение токена авторизации пользователя</p>

---
RequestBody
```
{
    "email": String,
    "password": String,
    "role": String
}
```
ResponseBody
```
{
    "token": String
}
```
</details>

---