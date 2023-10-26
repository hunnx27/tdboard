# js 파일 설명

## 폴더구조

```
├─ api
│  ├─ useAxois.js -----------> api 통신할때 사용
│
├─ plugin
│  ├─ ...-----------> 라이브러리 파일 모음
├─ common.js
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
