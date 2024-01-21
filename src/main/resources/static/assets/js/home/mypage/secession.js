import useAxios from '/assets/js/api/useAxios.js'

window.onload = function() {
    initPasswordCheck()


}

const initPasswordCheck = () => {
    const pwdConfirmBtn = document.getElementById('pwdConfirmBtn')
    const passwordCheckField = document.getElementById('passwordCheckField')
	const passwordElement = document.getElementById('passwordCheckInput');
    passwordElement.addEventListener('input', function() {
        
        const password = passwordElement.value;
        if(password){
            pwdConfirmBtn.disabled = false
        }else {
            pwdConfirmBtn.disabled = true
        }
        passwordCheckField.setAttribute('data-status', 'default');
    })
    
    pwdConfirmBtn.addEventListener('click', function(){
        getAuthMeApi(passwordElement.value)
    })
}

async function getAuthMeApi(password) {
 
    await useAxios.post('/api/v1/auth/validate/me',
    {
        'password': password
    }
    ,(res)=> {
        
        if(confirm('탈퇴하시겠습니까?')){
            //api call 탈퇴, 로그아웃
            alert('탈퇴성공')
            //success
            // location.replace('/')
        }
      
    },(err)=> {
        passwordCheckField.setAttribute('data-status', 'error');
        // alert(err.response.data.message)
    })
}
