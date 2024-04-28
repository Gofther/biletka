# Project Methods
1) POST - https://biletka-sv.ru:8443/{city}/event - Создаёт новое мероприятие
## Description: Создаёт новое мероприятие по введённым данным
   
### Request body - 
### {
    ```
    basic_information:{
      name: String,
      name_rus: String,
      event_type: String,
      genre: [String],
      age_rating: Integer,
      organizer: String,
      pushkin: Boolean,
      event_id_culture: Long,
      img_id: Long,
      show_in_poster: Boolean 
    },
    duration:{
      hours: Integer,
      minutes: Integer 
    },
    additional_information:{
      director: String,
      writer_or_artist: String,
      autor: String,
      actors: [String],
      tags: [String] 
    },
    web_widget:{
      signature: String,
      description: String,
      rating_yandex_afisha: Double,
      link: String
   ```
   ### }
    
   ### Response body: 
   ### {
    ```
    Id: Long,
    symbolicName: String
   ```
### }
#
2) GET - https://biletka-sv.ru:8443/{city}/event - Выводит все мероприятия
## Description: Выводит все мероприятия в выбранном городе

### Request body:
### {
   ```
   city: String
   ```
### }
   
### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
#
3) GET - https://biletka-sv.ru:8443/{city}/event/{eventSymbolicName}/{id} - Получение дат данного события в городе
## Description: Позволяет получить массив дат мероприятия в определенном городе

### Request body -
### {
   ```
   eventSymbolicName: String,
   city: String,
   id: Long
   ```
### }

### Response body:
### {
   ```
   dates: [String]
   ```
   ### }
#
4) GET - https://biletka-sv.ru:8443/{city}/event/{placeName} - Получение ивентов на площадке
## Description: Получение ивентов на площадке по названию города, названию площадки и дате

### Request body -
### {
   ```
   city: String,
   placeName: String,
   date: LocalDate
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
# 
5) GET - https://biletka-sv.ru:8443/{city}/event/{eventSymbolicName}/info/{id} - Получение деталей события
## Description: Позволяет получить всю информацию о событии

### Request body -
### {
   ```
   city: String,
   eventSymbolicName: String,
   id: Long
   ```
### }

### Response body:
### {
   ```
    basic_information:{
      name: String,
      name_rus: String,
      event_type: String,
      genre: [String],
      age_rating: Integer,
      organizer: String,
      pushkin: Boolean,
      event_id_culture: Long,
      img_id: Long,
      show_in_poster: Boolean 
    },
    duration:{
      hours: Integer,
      minutes: Integer 
    },
    additional_information:{
      director: String,
      writer_or_artist: String,
      autor: String,
      actors: [String],
      tags: [String] 
    },
    web_widget:{
      signature: String,
      description: String,
      rating_yandex_afisha: Double,
      link: String
   ```
### }
#
6) GET - https://biletka-sv.ru:8443/{city}/event/nearest-events} - Получение ближайших событий в городе
## Description: Позволяет получить массив мероприятий в определенном городе на неделю вперед

### Request body -
### {
   ```
   city: String,
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
#
7) GET - https://biletka-sv.ru:8443/{city}/event/popular} - Получение популярных событий
## Description: Получение популярных событий в указанном городе

### Request body -
### {
   ```
   city: String,
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
#
8) GET - https://biletka-sv.ru:8443/{city}/event/{eventSymbolicName}/{id}/places - Получение массива площадок ивента и небольшой информации
## Description: Получение массива площадок ивента и небольшой информации

### Request body -
### {
   ```
   city: String,
   eventSymbolicName: String,
   id: Long
   ```
### }

### Response body:
### {
   ```
   [
   {
    id: Long,
    filialName: String,
    address: String,
    timing: [String]
   }, ...
   ]
   ```
### }
#
9) DELETE - https://biletka-sv.ru:8443/{city}/event/remove - Удаление события по id
## Description: Удаление события по id
### Request body -
### {
   ```
   city: String,
   id: Long
   ```
### }

### Response body:
### {
   ```
   Id: Long,
   symbolicName: String
   ```
   ### }
#
10) GET - https://biletka-sv.ru:8443/{city}/event/type/{name} - Получение событий по указанному типу
## Description: Получение событий по указанному типу и началу поиска мероприятий

### Request body -
### {
   ```
   name: String,
   offset: Integer
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
# 
11) GET - https://biletka-sv.ru:8443/{city}/event/eight - Вывод восьми мероприятий
## Description: Вывод восьми мероприятий

### Request body -
### {
   ```
   city: String,
   offset: int
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
# 
12) GET - https://biletka-sv.ru:8443/{city}/event/pushkin - Вывод списка мероприятий по наличию пушкинской карты
## Description: Вывод списка мероприятий по наличию пушкинской карты

### Request body -
### {
   ```
   city: String,
   pushkin: Boolean,
   page: int
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
# 
13) GET - https://biletka-sv.ru:8443/{city}/event/age - Вывод списка мероприятий по возрастному ограничению
## Description: Вывод списка мероприятий по возрастному ограничению

### Request body -
### {
   ```
   city: String,
   age: Integer,
   page: int
   ```
### }

### Response body:
### {
   ```
   [
   {
   id: Long,
   name: String,
   symbolicName: String,
   nameRus: String, 
   type: String,
   duration: String,
   pushkin: Boolean,
   showInPoster: Boolean,
   imgFile: String,
   link: String
   }, ...
   ]
   ```
### }
# 
#
14) POST - https://biletka-sv.ru:8443/file/event - Загрузка изображения для мероприятия
## Description: Принимает файл jpg,png и тд и сохраняет в бд

### Request body - 
### {
```
file: MultipartFile
```
### }
    
### Response body: 
### {
```
id: Long,
imageName: String,
imageType: String
```
### }
#
15) POST - https://biletka-sv.ru:8443/file/organization - Загрузка файла организации
## Description: Принимает файл подписания услуг с орагнизацией и сохраняет в бд

### Request body -
### {
```
file: MultipartFile
```
### }

### Response body:
### {
```
id: Long
```
### }
#
15) POST - https://biletka-sv.ru:8443/file/hall - Загрузка изображения зала
## Description: Принимает файл jpg, png и тд и сохраняет в бд

### Request body -
### {
```
file: MultipartFile,
organizationId: Long,
id: Long
```
### }

### Response body:
### {
```
message: String
```
### }
#
16) GET - https://biletka-sv.ru:8443/file/{id} - Вывод изображения мероприятия
## Description: Вывод изображения мероприятия

### Request body -
### {
```
id: Long,
response: HttpServletResponse, 
request: HttpServletRequest
```
### }

### Response body:
### {
```
```
### }
#
#
17) PUT - https://biletka-sv.ru:8443/organization/place - Добавление площадки к организации
## Description: Добавление площадки к организации и занесение в базу данных

### Request body -
### {
```
places: [String],
organizationId: Long,
city: String
```
### }

### Response body:
### {
   ```
    id: Long,
    fullNameOrganization: String,
    legalAddress: String,
    postalAddress: Integer,
    contactPhone: String,
    email: String,
    activationCode: String,
    fullNameSignatory: String,
    positionSignatory: String,
    documentContract: String,
    INN: Integer,
    KPP: Integer,
    OGRN: Integer,
    OKTMO: Integer,
    KBK: Integer,
    namePayer: String,
    password: String,
    placeList:
      [
      {
      Long id,
      String name,
      String address,
      city:
         {
         cityId: Long,
         nameRus: String,
         nameEng: String
         }
      }, ...
      ]
    eventList:
      [
      {
      basic_information:
         {
         name: String,
         name_rus: String,
         event_type: String,
         genre: [String],
         age_rating: Integer,
         organizer: String,
         pushkin: Boolean,
         event_id_culture: Long,
         img_id: Long,
         show_in_poster: Boolean 
         },
      duration:
         {
         hours: Integer,
         minutes: Integer 
         },
      additional_information:
         {
         director: String,
         writer_or_artist: String,
         autor: String,
         actors: [String],
         tags: [String] 
         },
      web_widget:
         {
         signature: String,
         description: String,
         rating_yandex_afisha: Double,
         link: String
         },
      }, ...
      ]
   ```
### }
#
18) PUT - https://biletka-sv.ru:8443/organization/event - Добавление мероприятия к организации
## Description: Добавление мероприятия к организации и занесение в базу данных

### Request body -
### {
   ```
    eventId: Long,
    organizationId: Long
   ```
### }

### Response body:
### {
   ```
    id: Long,
    fullNameOrganization: String,
    legalAddress: String,
    postalAddress: Integer,
    contactPhone: String,
    email: String,
    activationCode: String,
    fullNameSignatory: String,
    positionSignatory: String,
    documentContract: String,
    INN: Integer,
    KPP: Integer,
    OGRN: Integer,
    OKTMO: Integer,
    KBK: Integer,
    namePayer: String,
    password: String,
    placeList:
      [
      {
      Long id,
      String name,
      String address,
      city:
         {
         cityId: Long,
         nameRus: String,
         nameEng: String
          }  
      }, ...
      ]
    eventList:
      [
      {
      basic_information:
         {
         name: String,
         name_rus: String,
         event_type: String,
         genre: [String],
         age_rating: Integer,
         organizer: String,
         pushkin: Boolean,
         event_id_culture: Long,
         img_id: Long,
         show_in_poster: Boolean 
         },
      duration:
         {
         hours: Integer,
         minutes: Integer 
         },
      additional_information:
         {
         director: String,
         writer_or_artist: String,
         autor: String,
         actors: [String],
         tags: [String] 
         },
      web_widget:
         {
         signature: String,
         description: String,
         rating_yandex_afisha: Double,
         link: String
         },
    }, ...
    ]
   ```
### }
#
19) PUT - https://biletka-sv.ru:8443/organization/{organisationId}/session/{sessionId}/change-session-status - Изменение статуса продажи сессии
## Description: Изменение статуса продажи сессии у организации

### Request body -
### {
   ```
    organizationId: Long,
    sessionId: Long,
    status: Boolean
   ```
### }

### Response body:
### {
   ```
    id: Long,
    name: String,
    nameRus: String,
    date: LocalDate,
    time: LocalTime,
    hall: String,
    status: String,
    ticketsSessionResponse:
      [
      {
      ticketId: Long,
      total: Integer,
      price: Integer,
      onSale: Integer,
      sales: Integer
      }, ...
      ]
   ```
### }
#
20) GET - https://biletka-sv.ru:8443/organization/sessions/{organisationId} - Получение сессий у организации
## Description: Получение сессий ивентов по id организации

### Request body -
### {
   ```
    organizationId: Long
   ```
### }

### Response body:
### {
   ```
    MassivePublicSessions:
      [
      {
      id: Long,
      name: String,
      symbolicName: String,
      date: LocalDateTime,
      issued: Integer,
      status: String
      }, ...
      ]
   ```
### }
#
21) GET - https://biletka-sv.ru:8443/organization/events/{organisationId} - Получение ивентов у организации
## Description: Получение ивентов по id организации

### Request body -
### {
   ```
    organizationId: Long
   ```
### }

### Response body:
### {
   ```
    MassivePublicEvents:
      [
      {
      id: Long,
      name: String,
      symbolicName: String,
      nameRus: String,
      type: String,
      duration: String,
      pushkin: Boolean,
      showInPoster: Boolean,
      imgFile: String,
      link: String
      }, ...
      ]
   ```
### }
#
22) GET - https://biletka-sv.ru:8443/organization/{organisationId}/session/{sessionId} - Получение информации сессии у организации
## Description: Получение информации сессии по id организации и id сессии

### Request body -
### {
   ```
    organizationId: Long,
    sessionId: Long
   ```
### }

### Response body:
### {
   ```
    id: Long,
    name: String,
    nameRus: String,
    date: LocalDate,
    time: LocalTime,
    hall: String,
    status: String,
    ticketsSessionResponse:
      [
      {
      ticketId: Long,
      total: Integer,
      price: Integer,
      onSale: Integer,
      sales: Integer
      }, ...
      ]
   ```
### }
#
23) DELETE - https://biletka-sv.ru:8443/organization/{organisationId}/event/{eventId} - Удаление ивента у организации
## Description: Удаление ивента по id организации и id мероприятия

### Request body -
### {
   ```
    organizationId: Long,
    eventId: Long
   ```
### }

### Response body:
### {
   ```
    name: String
   ```
### }
#
24) DELETE - https://biletka-sv.ru:8443/organization/{organisationId}/session/{sessionId} - Удаление сессии у организации
## Description: Удаление сессии по id организации и id сессии

### Request body -
### {
   ```
    organizationId: Long,
    sessionId: Long
   ```
### }

### Response body:
### {
   ```
    name: String,
    dateTime: LocalDateTime
   ```
### }
#
25) GET - https://biletka-sv.ru:8443/organization/{organizationId}/unevents - Вывод всех мероприятий
## Description: Вывод всех меропрятий, которых нет у организации

### Request body -
### {
   ```
    organizationId: Long,
   ```
### }

### Response body:
### {
   ```
    Event:
      [
      {
      id: Long,
      basic_information:
         {
         name: String,
         name_rus: String,
         event_type: String,
         genre: [String],
         age_rating: Integer,
         organizer: String,
         pushkin: Boolean,
         event_id_culture: Long,
         img_id: Long,
         show_in_poster: Boolean 
         },
      duration:
         {
         hours: Integer,
         minutes: Integer 
         },
      additional_information:
         {
         director: String,
         writer_or_artist: String,
         autor: String,
         actors: [String],
         tags: [String] 
         },
      web_widget:
         {
         signature: String,
         description: String,
         rating_yandex_afisha: Double,
         link: String
         }
      }, ...
      ]
   ```
### }
#
#
26) POST - https://biletka-sv.ru:8443/{city}/place - Создание площадки
## Description: Занесение площадки в базу данных

### Request body -
### {
   ```
    city: String,
    PlaceInfo placeInfo:
      {
      name: String,
      address: String
      }
   ```
### }

### Response body:
### {
   ```
    name: String,
    city: String,
    address: String,
    hall: [Integer]
   ```
### }
#
27) POST - https://biletka-sv.ru:8443/{city}/place/hall - Создание зала
## Description: Занесение зала в базу данных

### Request body -
### {
   ```
    city: String,
    hallCreationRequestDTO:
      {
      placeId: Long,
      name: String,
      floor: Integer,
      hallNumber: Integer,
      seatsCount: Integer,
      info: String,
      seatsGroupInfo: [String]
      }
   ```
### }

### Response body:
### {
   ```
    hallId: Long
   ```
### }
#
28) PUT - https://biletka-sv.ru:8443/{city}/place/hall - Добавление схемы зала
## Description: Добавление схемы зала

### Request body -
### {
   ```
    String scheme,
    Id: Long
   ```
### }

### Response body:
### {
   ```
    scheme: String
   ```
### }
#
29) GET - https://biletka-sv.ru:8443/{city}/place/hall/{organizationId} - Вывод всех залов у организации
## Description: Вывод всех залов у указанной организации

### Request body -
### {
   ```
    city: String,
    organizationId: Long
   ```
### }

### Response body:
### {
   ```
    responseDTO:
      [
      {
      id: Long,
      name: String,
      info: String,
      floor: int,
      hallNumber: int,
      numberSeats: int,
      scheme: String,
      seatGroupInfo: [String],
      place:
         {
         id: Long
         name: String,
         address: String,
         city:
            {
            cityId: Long,
            nameRus: String,
            nameEng: String
            }
         }
      }, ...
      ]
   ```
### }
#
30) GET - https://biletka-sv.ru:8443/{city}/place/all-organization-place/{organizationId} - Вывод всех площадок у организации
## Description: Вывод всех площадок у указанной организации

### Request body -
### {
   ```
    city: String,
    organizationId: Long
   ```
### }

### Response body:
### {
   ```
    place:
      [
      {
      id: Long
      name: String,
      address: String,
      city:
         {
         cityId: Long,
         nameRus: String,
         nameEng: String
         }
      }, ...
      ]
   ```
### }
#
31) GET - https://biletka-sv.ru:8443/{city}/place/scheme/{hallId} - Вывод схемы залла
## Description: Вывод схемы залла

### Request body -
### {
   ```
    hallId: Long
   ```
### }

### Response body:
### {
   ```
    schemeFloors:
      [
      {
      number: Integer,
      schemeRows:
         [
         {
         rowNumber: String,
         schemeSeats:
            [
            {
            occupied: boolean,
            number: String,
            group: String,
            position: String
            }, ...
            ]
         }, ...
         ]
      }, ...
      ]
   ```
### }
#
32) GET - https://biletka-sv.ru:8443/{city}/place/scheme/client/{sessionId} - Вывод схемы залла
## Description: Вывод всех площадок по сессии

### Request body -
### {
   ```
    sessionId: Long
   ```
### }

### Response body:
### {
   ```
    schemeResponse:
      {
      schemeFloors:
         [
         {
         number: Integer,
         schemeRows:
            [
            {
            rowNumber: String,
            schemeSeats:
               [
               {
               occupied: boolean,
               number: String,
               group: String,
               position: String
               }, ...
               ]
            }, ...
            ]
         }, ...
         ]
      group: [String],
      price: Integer
      }
   ```
### }
#
#
33) POST - https://biletka-sv.ru:8443/security/client - Регистрация пользователя
## Description: Позволяет зарегистрировать пользователя

### Request body -
### {
   ```
    client:
      {
      email: String,
      fullName: String,
      phoneNumber: String,
      password: String,
      birthday: Date
      }
   ```
### }
#
### Response body:
### {
   ```
    ClientResponse:
    {
    id: Long,
    fullName: String,
    phone: String,
    email: String,
    activationCode: String,
    password: String,
    birthday: Date,
    favoriteGenreId: int
    roleEnum:
      {
      value: String
      }
    basket:
      [
      {
      id: Long,
      rowNumber: Integer,
      seatNumber: Integer,
      isReserved: Boolean,
      isExtinguished: Boolean,
      activationCode: String,
      price: String,
      phone: String,
      email: String,
      fullName: String,
      info:
         {
         id: Long,
         price: Integer,
         sales: Integer,
         onSales: Integer,
         status: Boolean,
         session:
            {
            id: Long,
            Timestamp start,
            typeOfMovie:
               {
               id: Long,
               String mvtName
               }
            Event:
               {
               id: Long,
               basic_information:{
                  name: String,
                  name_rus: String,
                  event_type: String,
                  genre: [String],
                  age_rating: Integer,
                  organizer: String,
                  pushkin: Boolean,
                  event_id_culture: Long,
                  img_id: Long,
                  show_in_poster: Boolean 
                  },
               duration:{
                  hours: Integer,
                  minutes: Integer 
                  },
               additional_information:{
                  director: String,
                  writer_or_artist: String,
                  autor: String,
                  actors: [String],
                  tags: [String] 
                  },
               web_widget:{
                  signature: String,
                  description: String,
                  rating_yandex_afisha: Double,
                  link: String
                  }
               }
            place:
               {
               id: Long
               name: String,
               address: String,
               city:
                  {
                  cityId: Long,
                  nameRus: String,
                  nameEng: String
                  }
            roomLayout:
               {
               id: Long,
               name: String,
               info: String,
               floor: int,
               hallNumber: int,
               numberSeats: int,
               scheme: String,
               seatGroupInfo: [String],
               place:
                  {
                  id: Long
                  name: String,
                  address: String,
                  city:
                     {
                     cityId: Long,
                     nameRus: String,
                     nameEng: String
                     }
                  }
               }
            }
         }     
      }, ...
      ]
    favorites:
      [
      {
      place:
         {
         id: Long
         name: String,
         address: String,
         city:
            {
            cityId: Long,
            nameRus: String,
            nameEng: String
            }
         }
      }, ...
      ]
    }
   ```
### }
#
34) POST - https://biletka-sv.ru:8443/security/organization - Создание организации
## Description: Занесение юридического лица в базу данных

### Request body -
### {
   ```
    organizationRegistration:
      {
      fullNameOrganization: String,
      legalAddress: String,
      postalAddress: Integer,
      contactPhone: String,
      email: String,
      fullNameSignatory: String,
      positionSignatory: String,
      documentContract: String,
      INN: Integer,
      KPP: Integer,
      OGRN: Integer,
      OKTMO: Integer,
      KBK: Integer,
      namePayer: String,
      password: String,
      file: Long
      }
   ```
### }

### Response body:
### {
   ```
    organization:
      {
      id: Long
      fullNameOrganization: String,
      legalAddress: String,
      postalAddress: Integer,
      contactPhone: String,
      email: String,
      activationCode: String,
      fullNameSignatory: String,
      positionSignatory: String,
      documentContract: String,
      INN: Integer,
      KPP: Integer,
      OGRN: Integer,
      OKTMO: Integer,
      KBK: Integer,
      namePayer: String,
      password: String,
      placeList:
         [
         {
         id: Long
         name: String,
         address: String,
         city:
            {
            cityId: Long,
            nameRus: String,
            nameEng: String
            }
         }, ...
         ]
      eventList:
         [
         {
         id: Long,
         basic_information:
            {
            name: String,
            name_rus: String,
            event_type: String,
            genre: [String],
            age_rating: Integer,
            organizer: String,
            pushkin: Boolean,
            event_id_culture: Long,
            img_id: Long,
            show_in_poster: Boolean 
            },
         duration:
            {
            hours: Integer,
            minutes: Integer 
            },
         additional_information:
            {
            director: String,
            writer_or_artist: String,
            autor: String,
            actors: [String],
            tags: [String] 
            },
         web_widget:
            {
            signature: String,
            description: String,
            rating_yandex_afisha: Double,
            link: String
            }
         }, ...
         ]
      }
   ```
### }
#
35) POST - https://biletka-sv.ru:8443/security/auth - Получение токена авторизации
## Description: Позволяет получить токен для взаимодействия с системой

### Request body -
### {
   ```
   email: String,
   password: String,
   ```
### }

### Response body:
### {
   ```
    id: Long,
    role: String,
    type: String,
    accessToken: String
   ```
### }
#
36) POST - https://biletka-sv.ru:8443/security/organization/auth - Получение токена авторизации
## Description: Позволяет получить токен для взаимодействия с системой

### Request body -
### {
   ```
   email: String,
   password: String,
   ```
### }

### Response body:
### {
   ```
    id: Long,
    role: String,
    type: String,
    accessToken: String
   ```
### }
#
37) PUT - https://biletka-sv.ru:8443/security/invalidation - Недействительный токен авторизации
## Description: Помечает токен как недействительный, делая его непригодным для использования.

### Request body -
### {
   ```
    authorizationHeader: String
   ```
### }

### Response body:
### {
   ```
    email: String
   ```
### }
#
38) POST - https://biletka-sv.ru:8443/security/activate/{code} - Проверка кода активации
## Description: Проверка кода активации

### Request body -
### {
   ```
    code: String
   ```
### }

### Response body:
### {
   ```
    message: String
   ```
### }
#
39) GET - https://biletka-sv.ru:8443/security/client/{id} - Получение информации о клиенте
## Description: Получение информации о клиенте по id

### Request body -
### {
   ```
    id: Integer
   ```
### }

### Response body:
### {
   ```
    id: Long,
    fullName: String,
    phone: String,
    email: String,
    birthday: String
   ```
### }
#
40) GET - https://biletka-sv.ru:8443/security/organization/{id} -  Получение информации о организации
## Description: Получение информации о организации по id

### Request body -
### {
   ```
    id: Integer
   ```
### }

### Response body:
### {
   ```
    id: Long
    fullNameOrganization: String,
    legalAddress: String,
    postalAddress: Integer,
    contactPhone: String,
    email: String,
    fullNameSignatory: String,
    positionSignatory: String,
    INN: Integer,
    KPP: Integer,
    OGRN: Integer,
    OKTMO: Integer,
    KBK: Integer,
   ```
### }
#
#
41) POST - https://biletka-sv.ru:8443/{city}/event/session/create -  Создание сессии
## Description: Создание сессии на основе мероприятия

### Request body -
### {
   ```
    String city,
    sessionInfo:
      {
      datetime: LocalDateTime,
      typeOfMovie: String,
      eventSymbolicName: String,
      eventType: String,
      hallSchemeId: Long,
      basicPrice: Integer
      }
   ```
### }

### Response body:
### {
   ```
   session:
      {
      id: Long,
      name: String,
      symbolicName: String,
      type: String,
      hallNumber: Integer
      }
   ```
### }
#
42) GET - https://biletka-sv.ru:8443/{city}/event/session -  Сессии по дате и месту проведения
## Description: Получение массива сессий по фильтру(дате)

### Request body -
### {
   ```
    city: String,
    date: LocalDate,
    place: String
   ```
### }

### Response body:
### {
   ```
    PublicSession:
      [
      {
      id: Long,
      name: String,
      symbolicName: String,
      date: LocalDateTime,
      issued: Integer,
      status: String
      }, ...
      ]
   ```
### }
#
43) GET - https://biletka-sv.ru:8443/{city}/event/session/widget/{organizationId} -  Сессии по виджету
## Description: Получение массива сессий по виджету

### Request body -
### {
   ```
    city: String,
    organizationId: Long
   ```
### }

### Response body:
### {
   ```
    SessionWidgetResponse:
      [
      {
      id: Long,
      name: String,
      symbolicName: String,
      time: LocalTime
      date: LocalDate,
      hall: String,
      leftTicket: Integer,
      price: Integer
      }, ...
      ]
   ```
### }
#
44) GET - https://biletka-sv.ru:8443/{city}/event/session/getByEvent -  Сессии по ивенту дате и месту проведения
## Description: Получение массива сессий по фильтру(дате и ивенту)

### Request body -
### {
   ```
    city: String,
    eventId: Long,
    date: LocalDate
   ```
### }

### Response body:
### {
   ```
    SessionResponseByTicket:
      [
      {
      name: String,
      address: String,
      sessions:
         {
         id: Long,
         name: String,
         symbolicName: String,
         type:
            {
            id: Long,
            mvtName: String
            }
         LocalDateTime start,
         hallNumber: Integer,
         ticketPrice: Integer
         }
      }, ...
      ]
   ```
### }
#
#
45) GET - http://biletka-sv.ru:8443/ticket -  Создание билетов сессии
## Description: Позволяет заполнить и сохранить новые билеты сессии

### Request body -
### {
   ```
    ticketInfo:
        {
        sessionId: Long,
        price: Integer,
        onSale: Integer
        }
   ```
### }

### Response body:
### {
   ```
    id: Long,
    price: Integer,
    onSale: Integer,
    sales: Integer
   ```
### }
#
46) PUT - http://biletka-sv.ru:8443/ticket -  Обновление билетов сессии
## Description: Позволяет изменить и сохранить измененные билеты сессии

### Request body -
### {
   ```
    ticketEditInfo:
        {
        id: Long,
        price: Integer,
        sales: Integer,
        onSale: Integer
        }
   ```
### }

### Response body:
### {
   ```
    id: Long,
    price: Integer,
    onSale: Integer,
    sales: Integer
   ```
### }
#
47) GET - http://biletka-sv.ru:8443/ticket -  Вывод всех возможных билетов
## Description: Позволяет получить все возможные билеты

### Request body -
### {
   ```
   ```
### }

### Response body:
### {
   ```
    TicketsMassiveResponse:
        [
        {
        total: Integer,
        price: Integer,
        onSale: Integer,
        sales: Integer,
        ticketsResponses:
            [
            {
            id: Long,
            price: Integer,
            onSale: Integer,
            sales: Integer
            }, ...
            ]
        }, ...
        ]
   ```
### }
#
48) POST - http://biletka-sv.ru:8443/ticket/generate -  Генерирует qr-код
## Description: 

### Request body -
### {
   ```
   ```
### }

### Response body:
### {
   ```
   QRCodeImage: [byte] 
   ```
### }
#
49) GET - http://biletka-sv.ru:8443/ticket/getByUser -  Вывод билетов пользователя
## Description: Позволяет получить билеты пользователя по userId

### Request body -
### {
   ```
    id: Long
   ```
### }
#
### Response body:
### {
   ```
    [
    {
    TicketUserResponse:
       {
       id: Long,
       name: String,
       placeName: String,
       address: String,
       dateTime: String,
       hall: String,
       rowNumber: Integer,
       seatNumber: Integer,
       price: String,
       email: String,
       phone: String,
       fullName: String
       }
    }, ...
    ]
   ```
### }
#
50) POST - http://biletka-sv.ru:8443/ticket/buy -  Покупка билета
## Description: Позволяет купить билет на сеанс

### Request body -
### {
   ```
    buyRequest:
      {
      idSession: Long,
      fullName: String,
      email: String,
      tel: String,
      rawNumber: Integer,
      seatNumber: Integer,
      idUser: Long
      }
   ```
### }

### Response body:
### {
   ```
   id: Long,
   name: String,
   placeName: String,
   address: String,
   dateTime: String,
   hall: String,
   rowNumber: Integer,
   seatNumber: Integer,
   price: String,
   email: String,
   phone: String,
   fullName: String
   ```
### }
#
51) GET - http://biletka-sv.ru:8443/ticket/organization - Вывод билетов для организации
## Description: Позволяет получить массив билетов для организации

### Request body -
### {
   ```
    id: Long
   ```
### }

### Response body:
### {
   ```
   TicketsOrganizationResponse:
      [
      {
      address: String,
      placeName: String,
      ticketsSessionOrganizations:
         [
         {
         eventName: String,
         ticketOrganizations:
            [
            {
            id: Long,
            fullName: String,
            dateTime: String,
            rowNumber: Integer,
            seatNumber: Integer,
            status: Boolean
            }, ...
            ]
         }, ...
         ]
      }, ...
      ]
   ```
### }
#
52) PUT - http://biletka-sv.ru:8443/ticket/repayment - Погашение билета
## Description: Позволяет погасить билет

### Request body -
### {
   ```
    id: Long
   ```
### }

### Response body:
### {
   ```
   id: Long,
   fullName: String,
   dateTime: String,
   rowNumber: Integer,
   seatNumber: Integer,
   status: Boolean
   ```
### }
#
53) GET - http://biletka-sv.ru:8443/ticket/{id} - Вывод информации о билетах
## Description: Позволяет получить инфомармацию о билетах

### Request body -
### {
   ```
    id: Long
   ```
### }

### Response body:
### {
   ```
   id: Long,
   name: String,
   symbolicName: String,
   type:
      {
      id: Long,
      mvtName: String
      }
   start: LocalDateTime,
   hallNumber: Integer,
   ticketPrice: Integer
   ```
### }
#
54) GET - http://biletka-sv.ru:8443/ticket/buy - Вывод информации о билетах для покупки
## Description: Позволяет получить инфомармацию о билетах для покупки

### Request body -
### {
   ```
    ticketsInfoBuy:
      {
      id: Long,
      row: Integer,
      seat: Integer
      }
   ```
### }

### Response body:
### {
   ```
   idSession: Long,
   place: String,
   address: String,
   hall: Integer,
   rowNumber: Integer,
   seatNumber: Integer,
   price: String,
   dateTime: LocalDateTime
   ```
### }
#


   
