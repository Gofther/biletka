# Project Methods

## Security

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/security
</summary>
<p>Description: Регистрация обычного пользователя</p>

---
RequestBody
```
{
    "email": String,
    "password": String,
    "full_name": String,
    "phone_number": String,
    "birthday": Date
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---
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
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: orange; color: white; padding: 5px 10px; border-radius: 5px">PUT</span> https://localhost:8443/security/active
</summary>
<p>Description: Активация пользователя</p>

---
RequestBody
```
{
    "email": String,
    "code": String
}
```
ResponseBody
```
{
    "message": String
}
```
</details>

---