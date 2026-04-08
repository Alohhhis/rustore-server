package com.example.services

import com.example.models.ApplicationItem

class AppService {

    private val apps = listOf(
        ApplicationItem(
            id = 1,
            name = "Сбербанк",
            category = "Финансы",
            shortDescription = "Банковские услуги онлайн",
            fullDescription = "Полное описание приложения Сбербанк Онлайн. Управление счетами, переводы, платежи, инвестиции и многое другое.",
            developer = "Сбербанк",
            ageRating = "0+",
            apkUrl = "/downloads/uma.musicvk.apk",
            iconUrl = "/images/sber_logo.png",
            screenshots = listOf(
                "/images/sber_1.png",
                "/images/sber_2.png",
                "/images/sber_3.png"
            ),
            rating = 4.7
        ),
        ApplicationItem(
            id = 2,
            name = "2ГИС",
            category = "Транспорт",
            shortDescription = "Карты и справочники",
            fullDescription = "Полное описание приложения 2ГИС. Подробные карты, маршруты, справочники организаций, навигация и прокладка маршрутов.",
            developer = "2GIS",
            ageRating = "0+",
            apkUrl = "/downloads/uma.musicvk.apk",
            iconUrl = "/images/two_gis_logo.png",
            screenshots = listOf(
                "/images/two_gis_1.png",
                "/images/two_gis_2.png",
                "/images/two_gis_3.png"
            ),
            rating = 4.5
        ),
        ApplicationItem(
            id = 3,
            name = "Госуслуги",
            category = "Государственные",
            shortDescription = "Услуги государства онлайн",
            fullDescription = "Полное описание приложения Госуслуги. Получение государственных услуг, запись на приём, проверка статусов и уведомления.",
            developer = "Правительство РФ",
            ageRating = "0+",
            apkUrl = "/downloads/vkontakte.android-4_8_3.apk",
            iconUrl = "/images/gosusl_logo.png",
            screenshots = listOf(
                "/images/gosusl_1.png",
                "/images/gosusl_2.png",
                "/images/gosusl_3.png",
                "/images/gosusl_4.png"
            ),
            rating = 4.3
        ),
        ApplicationItem(
            id = 4,
            name = "VK Музыка",
            category = "Инструменты",
            shortDescription = "Музыка и плейлисты ВКонтакте",
            fullDescription = "Полное описание приложения VK Музыка. Слушай музыку, создавай плейлисты, сохраняй треки и делись с друзьями.",
            developer = "VK",
            ageRating = "12+",
            apkUrl = "/downloads/uma.musicvk.apk",
            iconUrl = "/images/vkmus_logo.png",
            screenshots = listOf(
                "/images/vkmus_1.png",
                "/images/vkmus_2.png",
                "/images/vkmus_3.png"
            ),
            rating = 4.2
        ),
        ApplicationItem(
            id = 5,
            name = "HomeScapes",
            category = "Игры",
            shortDescription = "Медитации и релакс",
            fullDescription = "Полное описание приложения HomeScapes. Медитации, расслабляющая музыка, звуки природы, дыхательные практики и улучшение сна.",
            developer = "CalmTech",
            ageRating = "6+",
            apkUrl = "/downloads/uptodown-com.playrix.homescapes.apk",
            iconUrl = "/images/homescapes_logo.png",
            screenshots = listOf(
                "/images/homescapes_1.png",
                "/images/homescapes_2.png",
                "/images/homescapes_3.png"
            ),
            rating = 4.6
        )
    )

    fun getAllApps(): List<ApplicationItem> = apps
    fun getAppById(id: Int): ApplicationItem? = apps.find { it.id == id }
}