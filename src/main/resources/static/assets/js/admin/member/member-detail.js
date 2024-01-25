import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function() {
    
    const memberId = document.getElementById('memberId').value
    getMemberId(memberId)

}

async function getMemberId(memberId) {
    await useAxios.get(`/api/v1/members/${memberId}`,
    {}
    ,(res)=> {
    initUserForm()
    handleSetUserInfo(res.data)

      
    },(err)=> {
        console.log('err',err)
    })
}

const initUserForm = () => {

    const selectRoleField = document.getElementById('selectRoleField');

    // "option" 클래스를 가진 버튼 요소들을 선택
    const optionButtons = document.querySelectorAll('.option');

    // 각 버튼에 클릭 이벤트 리스너 추가
    optionButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 클릭된 버튼의 data-option 값을 가져와서 selectJoinPathField의 data-value에 설정
            const dataOptionValue = button.dataset.option;
            selectRoleField.dataset.value = dataOptionValue;
        });
    });



    const modifyConfirmBtn = document.getElementById('modifyConfirmBtn');
    modifyConfirmBtn.addEventListener('click', () =>{
        updateUserApi()
    })

    const resetPwdBtn = document.getElementById('resetPwdBtn');
    resetPwdBtn.addEventListener('click', () =>{
        if(confirm('비밀번호 초기화 하시겠습니까?')){
            updateResetPwdApi()
        }
    })
    

}



async function updateResetPwdApi() {
    const memberId = document.getElementById('memberId').value
     
    await useAxios.post(`/api/v1/members/${memberId}/reset-password`,
    {}
    ,(res)=> {
        alert('비밀번호 초기화가 되었습니다.')
        location.reload()
    },(err)=> {
        console.log('err',err)
    })
}


async function updateUserApi() {
    const memberId = document.getElementById('memberId').value
    const selectRoleField = document.getElementById('selectRoleField');
    const email = document.getElementById('email').innerText
 
    await useAxios.put(`/api/v1/members/${memberId}`,
    {
        "email": email,
        "role": selectRoleField.dataset.value 
    }
    ,(res)=> {
        alert('정보가 수정되었습니다.')
        location.reload()
    },(err)=> {
        console.log('err',err)
    })
}


function handleSetUserInfo(data){
    
    document.getElementById('userId').innerText = data.userId
    document.getElementById('userName').innerText = data.username
    document.getElementById('email').innerText = data.email
    document.getElementById('createdAtText').innerText = useFilters().YYYYMMDD( data.createdAt,'YYYY-MM-DD HH:mm:ss')
    document.getElementById('phone').innerText = data.phone
    
    document.getElementById('selectRoleField').dataset.value = data.role
    document.querySelectorAll('.option').forEach((element)=>{
        if (element.dataset.option === data.role) {
            const ementText = element.innerText;
            document.getElementById('selectRoleField').innerText = ementText;
        }
    })

    
}
