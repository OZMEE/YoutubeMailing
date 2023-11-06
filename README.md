# YoutubeMailing - long-polling bot for telegram
<h1>Описание</h1>
<p>Бот предназначен для уведомления о выходе новых видео на ютуб каналах. Возможно, когда вы это читаете,
бот еще работает: https://t.me/GigaChadurlNe_bot</p>
<h2>Как пользоваться?</h2>
<ul>
  <li>Чтобы бот уведомлял о выходе новых видео на определенном канале, нужно отправить ему ссылку на этот ютуб канал,
  не важно на какую вкладку, главное чтобы ссылка содержала название канала:</li>
  ![image](https://github.com/OZMEE/YoutubeMailing/assets/138571586/3fcb5799-d7fa-4e41-884e-ff03f136f536)
  <li>Просмотреть добавленные каналы можно с помощью команды: /my_channels</li>
  <li>Удалять каналы можно с помощью кнопок, прикрепленных к сообщению /my_channels</li>
</ul>
<h1>Запуск бота за 5 шагов (без учета создания бота через BotFather)</h1>
<ol>
  <li>Скачать репозиторий</li>
  <li>Подключить базу данных Postgresql</li>
  <li>В папке main создать файл resources -> application.properties и настроить этот файл для работы с бд, потом указать диалект для hibernate и данные для telegram api: "bot.token" и "bot.name":</li>
  ![image](https://github.com/OZMEE/YoutubeMailing/assets/138571586/6d8b066f-7488-4188-975c-cec8280d78dd)
  ![image](https://github.com/OZMEE/YoutubeMailing/assets/138571586/d17b8abf-8aed-4658-b8e0-7b2e56c56290)
  <li>Запустить файл db_createTables.sql в папке db_migrations</li>
  <li>И запустить в папке java класс с main методом</li>
</ol>
