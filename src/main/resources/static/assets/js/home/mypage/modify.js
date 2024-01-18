window.onload = function() {
    initPasswordCheck()


}

const initPasswordCheck = () => {
    const pwdConfirmBtn = document.getElementById('pwdConfirmBtn')
   
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
    const userModifyForm = document.getElementById('userModifyForm')
    const passwordCheckField = document.getElementById('passwordCheckField')
    const passwordChekForm = document.getElementById('passwordChekForm')
    pwdConfirmBtn.addEventListener('click', function(){
        const password = passwordElement.value;
        // api call
        if(password === 'user1'){
            userModifyForm.style.display = 'block';
            passwordChekForm.style.display = 'none';
        }else {
            userModifyForm.style.display = 'none';
            passwordCheckField.setAttribute('data-status', 'error');
         
        }
    })
}