import useAxios from '/assets/js/api/useAxios.js'

const userIdRegex = /^[a-z]+[a-z0-9]{5,20}$/g;
const nameRegex = /^[a-zA-Z가-힣\s]+$/;
const passwordRegex = /^(?=.*[A-Z])(?=.[a-z])(?=.*\d)(?=.*[-`~!@#$%^&*()_+=?/\[\]])[A-Za-z\d\[\]-`~!@#$%^&*()_+=?/]{8,}$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^01[0-9]*\d{3,4}\d{4}$/; 

window.onload = function() {
    validateUserId() // 아이디 실시간 체크
    validateName() // 이름 실시간 체크
    validatePassword() // 비밀번호 실시간 체크
    validateEmail() // 이메일 실시간 체크
    validatePhone() // 휴대전화 실시간 체크

    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const birthdayField = document.getElementById('birthdayField')
            birthdayField.setAttribute('data-status', 'active');
            validationButtonVisible();
        }
    });

    const buttons = document.querySelectorAll('.genderBtn');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
          // 모든 버튼에서 'active' 클래스를 제거
          buttons.forEach(btn => btn.classList.remove('active'));
          button.classList.add('active');
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
        validationButtonVisible()

    })

    // 가입경로
    const selectJoinPathField = document.getElementById('selectJoinPathField');

    // "option" 클래스를 가진 버튼 요소들을 선택
    const optionButtons = document.querySelectorAll('.option');

    // 각 버튼에 클릭 이벤트 리스너 추가
    optionButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 클릭된 버튼의 data-option 값을 가져와서 selectJoinPathField의 data-value에 설정
            const dataOptionValue = button.dataset.option;
            selectJoinPathField.dataset.value = dataOptionValue;
        });
    });

    //회원가입 버튼
    const joinBtn = document.getElementById('joinBtn')
    joinBtn.addEventListener('click', () =>{
        handleJoin()
    })
}


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
        validationButtonVisible()
    });
}
// 이름 실시간 체크
function validateName() {
    const userName = document.getElementById('userName');
    const userNameField = document.getElementById('userNameField');
    const userNameFieldChild = userNameField.querySelector('.error-text'); 
    userName.addEventListener('input', function() {
        const name = userName.value;

        if (!nameRegex.test(name)) {
            userNameField.setAttribute('data-status', 'error');
            userNameFieldChild.innerText = '영문, 한글만 입력 가능합니다'
         
        } else {
            userNameField.setAttribute('data-status', 'active');
            userNameFieldChild.innerText = '';
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
    const emailFieldChild = emailField.querySelector('.error-text'); 
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
    const userIdCheckBtn = document.getElementById('userIdCheckBtn')
    let count = 0;
    textfield.forEach(textfield => {
        const input = textfield.querySelector('input').value
        if(input && textfield.dataset.status === 'active') {
            if(!userIdCheckBtn.disabled){
                count ++;
            }
        }else {
            count ++;
        }
        if(count > 0){
            joinBtn.disabled = true
        }else {
            joinBtn.disabled = false
        }
        
    })
   
    
}

async function handleJoin() {
    const userId = document.getElementById('userId').value;
    const username = document.getElementById('userName').value;
    const password = document.getElementById('password').value;
    const email = document.getElementById('email').value;
    const phone = document.getElementById('phone').value;
    const birthday = document.getElementById('birthday').value;
    const selectJoinPathField = document.getElementById('selectJoinPathField');
    let sex = null
    const buttons = document.querySelectorAll('.genderBtn');
    buttons.forEach(button => {
        if (button.dataset.status === 'active') {
            sex = button.dataset.value;
        }
      });
    

    await useAxios.post('/api/v1/auth/join',
    {
        "username": username,
        "userId": userId,
        "sex": sex,
        "email": email,
        "phone": phone,
        "birthday": birthday,
        "channel": selectJoinPathField.dataset.value,
        "password": password,
        "role": "ROLE_USER"
    }
    ,(res)=> {
        alert('회원가입이 완료되었습니다!')
        location.href='/auth/login'
      
    },(err)=> {
        // console.log('err',err)
        alert(err.response.data.message)
    })
  }