import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pawcare.dogcat.R


val Pretendard = FontFamily(
    Font(R.font.pretendard_thin, FontWeight.Thin),           // 100
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight), // 200
    Font(R.font.pretendard_light, FontWeight.Light),         // 300
    Font(R.font.pretendard_regular, FontWeight.Normal),      // 400
    Font(R.font.pretendard_medium, FontWeight.Medium),       // 500
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),  // 600
    Font(R.font.pretendard_bold, FontWeight.Bold),           // 700
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold) // 800
)

data class CustomTypography(
    // 큰 제목 (예: 화면 메인 제목)
    val h1: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),
    // 중간 제목 (예: 섹션 제목)
    val h2: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    // 작은 제목 (예: 카드 제목)
    val h3: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    // 강조된 본문 (예: 중요 정보)
    val body1Emphasis: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // 기본 본문
    val body1: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // 작은 본문 (예: 부가 설명)
    val body2: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    // 버튼 텍스트
    val button: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // 캡션 (예: 이미지 설명, 날짜)
    val caption: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    // 매우 작은 텍스트 (예: 카운터, 배지)
    val small: TextStyle = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.ExtraLight,
        fontSize = 10.sp,
        lineHeight = 14.sp
    )
)

// 타이포그래피 인스턴스 생성
val CustomTypographySet  = CustomTypography()
