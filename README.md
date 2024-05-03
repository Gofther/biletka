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
<p>Description: Регистрация пользователя</p>

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
    "message": String
}
```
</details>

---
<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/security/organization
</summary>
<p>Description: Регистрация организации</p>

---
RequestBody
```
{
    "email": String,
    "password": String,
    "contact_phone": String,
    "full_name_organization": String,
    "full_name_signatory": String,
    "inn": String (max size 10),
    "kbk": String (max size 20),
    "kpp": String (max size 9),
    "ogrn": String (max size 13),
    "oktmo": String (max size 11),
    "legal_address": String,
    "name_payer": String,
    "position_signatory": String,
    "postal_address": String (max size 6)
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
## Place

<details>
<summary style="font-size: 17px">
<span  style="font-weight: 600; background-color: green; color: white; padding: 5px 10px; border-radius: 5px">POST</span> https://localhost:8443/place
</summary>
<p>Description: Создания площадки и добавления её к организации</p>
<p>Authorization - TRUE</p>

---
RequestBody
```
{
    "address": String,
    "place_name": String,
    "city": String
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