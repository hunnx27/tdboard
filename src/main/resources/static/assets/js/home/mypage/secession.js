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
        const password = passwordElement.value;
        // api call
        if(password === 'user1'){
            if(confirm('탈퇴하시겠습니까?')){
                //api call 탈퇴, 로그아웃
                //success
                location.replace('/')
            }
        }else {
            passwordCheckField.setAttribute('data-status', 'error');
        }
    })
}