const userIdRegex = /^[a-z]+[a-z0-9]{5,20}$/g;
const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^\d{3}-\d{4}-\d{4}$/;

$(function(){
    validateUserId() // 아이디 실시간 체크
    validatePassword() // 비밀번호 실시간 체크
});


// 아이디 실시간 체크
function validateUserId(){
    const userIdElement = document.getElementById('userId');
    const userIdField = document.getElementById('userIdField');
    const userIdFieldChild = userIdField.querySelector('.error-text'); 
    userIdElement.addEventListener('input', function() {
        const userId = userIdElement.value;

        if (!userIdRegex.test(userId)) {
            userIdField.setAttribute('data-status', 'error');
            userIdFieldChild.innerText = '영문자 또는 영문,숫자만(5~20자) 입력 가능합니다'
         
        } else {
            userIdField.setAttribute('data-status', 'active');
            userIdFieldChild.innerText = '';
        }
    });
}
// 비밀번호 실시간 체크
function validatePassword(){
    const passwordElement = document.getElementById('password');
    const passwordField = document.getElementById('passwordField');
    const passwordFieldChild = passwordField.querySelector('.error-text'); 
    passwordElement.addEventListener('input', function() {
        const password = passwordElement.value;
    
        if (!passwordRegex.test(password)) {
            passwordField.setAttribute('data-status', 'error');
            passwordFieldChild.innerText = '영문 숫자 조합 8~25자 이상 입력 가능합니다'
         
        } else {
            passwordField.setAttribute('data-status', 'active');
            passwordFieldChild.innerText = '';
        }
    });

    const confirmPassword = document.getElementById('confirmPassword');
    const confirmPasswordField = document.getElementById('confirmPasswordField');
    const confirmPasswordFieldChild = confirmPasswordField.querySelector('.error-text');
    confirmPassword.addEventListener('input', function() {
        const password = confirmPassword.value;

        if (password !== passwordElement.value) {
            confirmPasswordField.setAttribute('data-status', 'error');
            confirmPasswordFieldChild.innerText = '입력하신 비밀번호가 다릅니다.'
         
        } else {
            confirmPasswordField.setAttribute('data-status', 'active');
            confirmPasswordFieldChild.innerText = '';
        }
    });
}

function validateForm() {
    const userId = document.getElementById('userId').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;

    if (!userIdRegex.test(userId)) {
      alert('아이디는 영문자와 숫자만 입력 가능합니다.');
      return;
    }

    if (!passwordRegex.test(password)) {
      alert('비밀번호는 최소 6자 이상 입력하세요.');
      return;
    }

    if (password !== confirmPassword) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
      return;
    }

    if (!emailRegex.test(email)) {
      alert('올바른 이메일 주소를 입력하세요.');
      return;
    }

    if (!phoneRegex.test(phone)) {
      alert('올바른 휴대전화번호 형식을 입력하세요. (예: 010-1234-5678)');
      return;
    }

    alert('회원가입이 완료되었습니다!');
  }