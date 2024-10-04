SWAGGER дока:
для получения документации, нужно запустить проект и перейти по ссылке:
swagger документация: http://localhost:8080/swagger-ui/index.html

DOCKER:
Для запуска проекта с docker-compose нужно перейти в репозиторий
просто ссылка: https://github.com/LevorukiyI/book_service_app
GIT: https://github.com/LevorukiyI/book_service_app.git

и запустить проект: docker-compose up --build

ЛОКАЛЬНО:
Чтобы запустить проект локально нужно зайти в application-local.yml и ввести данные от вашей локальной бд.
затем изменить профиль сборки maven на local и обновить сборку мавен
После чего можно запустить проект локально

ТЕСТЫ:
Для запуска тестов нужно зайти в application-test.yml и ввести данные от вашей локальной тестовой бд.
затем изменить профиль сборки maven на test и обновить сборку мавен
После чего можно запустить тесты
