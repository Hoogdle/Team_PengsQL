package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


// 굳이 구현할 필요는 없을 것 같아 뼈대만 작성하였습니다.

// 아래의 ViewModel 클래스가 실질적으로 데이터를 처리하는 역할을 합니다. (백엔드와 비슷한 개념)
class TmpDesignViewModel : ViewModel(){

    // 값이 변경되면 이를 반영할 수 있게 MutableStateFlow를 사용
    // viewModel 내부에선만 값을 변경할 수 있고 외부에서는 변경할 수 없도록 아래와 같은 형태를 많이 사용합니다.
    // 즉, viewmodel 외부에서는 값을 read만, 내부에서는 모두 가능하게 할 수 있게 하는 것이 아래의 코드가 되겠습니다.
    private val _fileData = MutableStateFlow(sampleFiles)
    val fileData: StateFlow<List<TmpDesignData>> = _fileData.asStateFlow()


    // 파일 추가 함수 입니다. 완성 하지는 않았고 예시로 작성한 코드니 참고만 해주세요.
    private fun addFile(
        name: String,
        size: Float
    ){
        _fileData.value.add(
            TmpDesignData(
                fileName = name,
                size = size
            )
        )
    }
}