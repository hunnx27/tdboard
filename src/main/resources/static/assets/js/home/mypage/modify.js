import useAxios from '/assets/js/api/useAxios.js'

const userIdRegex = /^[a-z]+[a-z0-9]{5,20}$/g;
const passwordRegex = /^(?=.*[A-Z])(?=.[a-z])(?=.*\d)(?=.*[-`~!@#$%^&*()_+=?/\[\]])[A-Za-z\d\[\]-`~!@#$%^&*()_+=?/]{8,}$/;
const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const phoneRegex = /^01[0-9]*\d{3,4}\d{4}$/; 

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

            //success
            getUserApi()

            userModifyForm.style.display = 'block';
            passwordChekForm.style.display = 'none';

            
        }else {
            userModifyForm.style.display = 'none';
            passwordCheckField.setAttribute('data-status', 'error');
         
        }
    })
}

const initUserForm = () => {
    validatePassword() // 비밀번호 실시간 체크
    validateEmail() // 이메일 실시간 체크
    validatePhone() // 휴대전화 실시간 체크
    const buttons = document.querySelectorAll('.genderBtn');
    buttons.forEach(button => {
        button.addEventListener('click', function() {
          // 모든 버튼에서 'active' 클래스를 제거
          buttons.forEach(btn => btn.classList.remove('active'));
          button.classList.add('active');
        });
      });

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


    const modifyConfirmBtn = document.getElementById('modifyConfirmBtn');
    modifyConfirmBtn.addEventListener('click', () =>{
        updateUserApi()
    })
}

async function getUserApi(){
    await useAxios.get('/api/v1/users/me',
    {}
    ,(res)=> {
    console.log('res',res.originalBody.data)
    initUserForm()
    handleSetUserInfo(res.originalBody.data)

      
    },(err)=> {
        console.log('err',err)
    })

}

async function updateUserApi() {
    const username = document.getElementById('userName').value
    const email = document.getElementById('email').value
    const password = document.getElementById('password').value
    const phone = document.getElementById('phone').value
    const selectJoinPathField = document.getElementById('selectJoinPathField');
    let sex = null
    const buttons = document.querySelectorAll('.genderBtn');
    buttons.forEach(button => {
        if (button.dataset.status === 'active') {
            sex = button.dataset.value;
        }
      });
      // 생년월일, 채널 , 비밀번호 추가 작업 필요함
    await useAxios.put('/api/v1/users/me',
    {
        "username": username,
        "sex": sex,
        "email": email,
        "phone": addHyphens(phone),
        "birthday": "1986-01-20",
        "channel": selectJoinPathField.dataset.value,
        "password": password,
        "role": "ROLE_USER"
    }
    ,(res)=> {
        alert('정보가 수정되었습니다.')
      
    },(err)=> {
        console.log('err',err)
    })
}


function handleSetUserInfo(data){
    document.getElementById('userId').value = data.userId
    document.getElementById('userName').value = data.username
    document.getElementById('email').value = data.email
    document.getElementById('phone').value = data.phone.replace(/-/g, '');
    const buttons = document.querySelectorAll('.genderBtn');
    buttons.forEach(button => {
        if (button.dataset.value === data.sex) {
            button.dataset.status = 'active'
        }else {
            button.dataset.status = 'default'
        }
      });
    
    document.getElementById('selectJoinPathField').dataset.value = data.channel
    document.querySelectorAll('.joinPathOption').forEach((element)=>{
        if (element.dataset.option === data.channel) {
            const childelementText = element.innerText;
            document.getElementById('selectJoinPathText').innerText = childelementText;
        }
    })

    
}
function addHyphens(phoneNumber) {
    // 정규식을 사용하여 숫자만 남기고, 새로운 형식에 맞게 하이픈을 추가합니다.
    const cleanedNumber = phoneNumber.replace(/\D/g, ''); // 숫자 이외의 문자 제거
    const formattedNumber = cleanedNumber.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
    return formattedNumber;
}
// 비밀번호 실시간 체크
function validatePassword(){
    const passwordElement = document.getElementById('password');
    const passwordField = document.getElementById('passwordField');
    const passwordFieldChild = passwordField.querySelector('.error-text'); 
    passwordElement.addEventListener('input', function() {
        const password = passwordElement.value;
    
        if (password && !passwordRegex.test(password)) {
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
    const joinBtn = document.getElementById('modifyConfirmBtn')
    const textfield = document.querySelectorAll('.textfield');
    let count = 0
    textfield.forEach(textfield => {
        const input = textfield.querySelector('input').value
        if(input && (textfield.dataset.status === 'error')) {
            count ++;
        }
        if(count > 0){
            joinBtn.disabled = true
        }else {
            joinBtn.disabled = false
        }
    })
    
}