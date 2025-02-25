import androidx.compose.ui.unit.dp

object Paddings {
    // 기본 패딩
    val small = 4.dp
    val medium = 8.dp
    val large = 16.dp
    val extraLarge = 24.dp
    val huge = 50.dp

    // 특정 용도의 패딩
    object Screen {
        val horizontal = 16.dp
        val vertical = 16.dp
    }

    object Card {
        val horizontal = 16.dp
        val vertical = 12.dp
        val between = 8.dp  // 카드 내부 요소들 사이 간격
    }

    object Button {
        val horizontal = 16.dp
        val vertical = 8.dp
    }

    object Icon {
        val small = 16.dp
        val medium = 24.dp
        val large = 32.dp
    }

    object Space {  // 요소들 사이의 간격
        val small = 4.dp
        val medium = 8.dp
        val large = 16.dp
        val extraLarge = 24.dp
    }

}