import useAxios from '/assets/js/api/useAxios.js'

window.onload = function() {
    const userId = document.getElementById('userId')
    const userName = document.getElementById('userName')
    const email = document.getElementById('email')
    const pwdFindConfirm = document.getElementById('pwdFindConfirm');
   
    userId.addEventListener('input', function() {
        if(userId.value && userName.value && email.value){
            pwdFindConfirm.disabled = false
        }else {
            pwdFindConfirm.disabled = true
        }
    })

    userName.addEventListener('input', function() {
        if(userId.value && userName.value && email.value){
            pwdFindConfirm.disabled = false
        }else {
            pwdFindConfirm.disabled = true
        }
    })
    email.addEventListener('input', function() {
        if(userId.value && userName.value && email.value){
            pwdFindConfirm.disabled = false
        }else {
            pwdFindConfirm.disabled = true
        }
    })

    pwdFindConfirm.addEventListener('click', function(){
        
        handleSearchPwd()
    })

}

async function handleSearchPwd() {
    const userId = document.getElementById('userId').value
    const userName = document.getElementById('userName').value
    const email = document.getElementById('email').value
    await useAxios.post('/api/v1/auth/find/id',
    {
        "username": userName,
        "email": email,
        "userId": userId
    }
    ,(res)=> {
        alert('초기화 비밀번호는 가입했던 휴대전화번호입니다.')
        location.reload()
    },(err)=> {
        // console.log('err',err)
        alert(err.response.data.message)
    })
}