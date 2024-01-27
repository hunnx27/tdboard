# js 파일 설명

## 폴더구조

```
├─ api
│  ├─ useAxois.js -----------> api 통신할때 사용
│
├─ plugin
│  ├─ ...-----------> 라이브러리 파일 모음
├─ common.js
├─ file.js -----------> 파일첨부 등록 한개만 가능
├─ file-list.js -----------> 파일첨부 등록 여러개 가능
├─ test.js -----------> 테스트 하기위한 파일
```

## useAxios 사용법

```
// 예시
import useAxios from '/assets/js/api/useAxios.js'

await useAxios.get('/api/user/test'
        ,(res)=> {
            // 성공시 
            console.log('res',res) // res = [{..},{...}]
        },(err)=> {
            console.log('err',err) // err = {code: string , message: string , ...}
        })
```

## file.js, file-list.js 사용법

```
// mustache 파일에 상황에 맞게 추가
{[>layout/admin-file-list]}  <!-- 첨부파일 등록 할때(어드민용)-->
{[>layout/file-list]} <!-- 첨부파일 등록 할때-->
{[>layout/file-download-list]} <!-- 첨부파일 다운로드만 가능-->
// 예시
import createFileListModule from "/assets/js/file.js"
const fileListModule = createFileListModule()

window.onload = function() {
    // 파일첨부 세팅
    fileListModule.initFileSetting()

    // 파일첨부 기존 값이 있으면 세팅
     const list = [
        {
            storedPath: 
            "/upload/BOARD/240127/a44683982529411aa7824098c5c8b47d.png",
            originalName: 'a44683982529411aa7824098c5c8b47d.png'
        },
        {
            storedPath: 
            "/upload/BOARD/240127/a44683982529411aa7824098c5c8b47d33.png",
            originalName: 'a44683982529411aa7824098c5c8b4733d.png'
        }
    ]
    fileListModule.initFileSetting(list)

}

// 파일 업로드
async function fileUpload(){
    const dataTransfer = fileListModule.dataTransfer
    // FormData 객체 생성
    const formData = new FormData();
    formData.append('uploadType','EQUIPMENT')

    // 추가된 파일 있는지 체크하기 위한 로직
    for (const file of dataTransfer.files) {
        if(!file.id){
            formData.append('files', file);
        }
  
    }

    await useAxios.postMultipart(`/api/v1/files`,
    formData
    ,(res)=> {
        const files = []
        // 이미 업로드 되어있는 파일 id를 리스트에 추가
        for (const file of dataTransfer.files) {
            if(file.id){
                files.push(file.id)
            }
        }
        // 새로 추가된 파일의 id를 리스트에 추가
        res.data?.forEach((file)=>{
            files.push(file.id)
        })
        // console.log(files)
        saveApi(files)
    },(err)=> {
        console.log('error',err)
        // alert(err.response.data.message)
    })
  
}
  
```
