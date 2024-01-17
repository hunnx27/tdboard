const userIdRegex = /^[a-z]+[a-z0-9]{5,20}$/g;
const passwordRegex = /^(?=.*[A-Z])(?=.[a-z])(?=.*\d)(?=.*[-`~!@#$%^&*()_+=?/\[\]])[A-Za-z\d\[\]-`~!@#$%^&*()_+=?/]{8,}$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^01[0-9]*\d{3,4}\d{4}$/; 

$(function(){
    validateUserId() // 아이디 실시간 체크
    validatePassword() // 비밀번호 실시간 체크
    validateEmail() // 이메일 실시간 체크
    validatePhone() // 휴대전화 실시간 체크

    const buttons = document.querySelectorAll('.genderBtn');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
          // 클릭한 버튼의 data-status 값을 업데이트
          buttons.forEach(btn => {
            btn.dataset.status = (btn === button && btn.dataset.status !== 'active') ? 'active' : '';
          });
    
          // 모든 버튼에서 'active' 클래스를 제거
          buttons.forEach(btn => btn.classList.remove('active'));
    
          // 현재 활성화된 버튼에 'active' 클래스 추가
          if (button.dataset.status === 'active') {
            button.classList.add('active');
          }
        });
      });

    // 중복확인 클릭시
    const userIdCheckBtn = document.getElementById('userIdCheckBtn');
    const userIdElement = document.getElementById('userId');
    userIdCheckBtn.addEventListener('click', function(){
        // 아이디 중복 체크 api호출
        // 성공하면 아래 로직(정책필요)
        userIdCheckBtn.disabled = true
        userIdElement.readOnly = true
        

    })


    const selectJoinPathField = document.getElementById('selectJoinPathField')
    const selectJoinPathBtn = selectJoinPathField.querySelector('button')
    selectJoinPathBtn.addEventListener('click', function(){
        if(selectJoinPathField.dataset.status === 'active'){
            selectJoinPathField.dataset.status = ''
        }else{
            selectJoinPathField.dataset.status = 'active'
        }
    })
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
            userIdFieldChild.innerText = '영문 대소문자, 숫자, 특수문자 조합하여 8~16자 입력가능합니다'
         
        } else {
            userIdField.setAttribute('data-status', 'active');
            userIdFieldChild.innerText = '';
        }
        validationButtonVisible()
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
            passwordFieldChild.innerText = '영문 대 소문자, 숫자, 특수문자를 조합해서 사용하세요'
         
        } else {
            passwordField.setAttribute('data-status', 'active');
            passwordFieldChild.innerText = '';
        }
        validationButtonVisible()
    });

    const confirmPassword = document.getElementById('confirmPassword');
    const confirmPasswordField = document.getElementById('confirmPasswordField');
    const confirmPasswordFieldChild = confirmPasswordField.querySelector('.error-text');
    confirmPassword.addEventListener('input', function() {
        const password = confirmPassword.value;

        if (password !== passwordElement.value) {
            confirmPasswordField.setAttribute('data-status', 'error');
            confirmPasswordFieldChild.innerText = '입력한 비밀번호와 일치하지 않습니다'
         
        } else {
            confirmPasswordField.setAttribute('data-status', 'active');
            confirmPasswordFieldChild.innerText = '';
        }
        validationButtonVisible()
    });
}
// 이메일 실시간 체크
function validateEmail(){
    const emailElement = document.getElementById('email');
    const emailField = document.getElementById('emailField');
    const emailFieldChild = userIdField.querySelector('.error-text'); 
    emailElement.addEventListener('keyup', function() {
        const email = emailElement.value;

        if (!emailRegex.test(email)) {
            emailField.setAttribute('data-status', 'error');
            emailFieldChild.innerText = '이메일 형식으로 입력해주세요'
         
        } else {
            emailField.setAttribute('data-status', 'active');
            emailFieldChild.innerText = '';
        }
        validationButtonVisible()
    });
}
// 휴대전화번호 실시간 체크
function validatePhone(){
    const phoneElement = document.getElementById('phone');
    const phoneField = document.getElementById('phoneField');
    const phoneFieldChild = phoneField.querySelector('.error-text'); 
    phoneElement.addEventListener('input', function() {
        const phone = phoneElement.value;

        if (!phoneRegex.test(phone)) {
            phoneField.setAttribute('data-status', 'error');
            phoneFieldChild.innerText = '휴대전화번호에 맞게 숫자로만 입력해주세요'
         
        } else {
            phoneField.setAttribute('data-status', 'active');
            phoneFieldChild.innerText = '';
        }
        validationButtonVisible()
    });
}

function validationButtonVisible() {
    const joinBtn = document.getElementById('joinBtn')
    const textfield = document.querySelectorAll('.textfield');
    textfield.forEach(textfield => {
        const input = textfield.querySelector('input').value
        if(input && textfield.dataset.status === 'active') {
            joinBtn.disabled = false
        }else {
            joinBtn.disabled = true
        }
    })
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