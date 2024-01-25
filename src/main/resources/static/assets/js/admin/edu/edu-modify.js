import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function() {
    
    const educationId = document.getElementById('educationId').value
    getEducationId(educationId)

}

async function getEducationId(educationId) {
    await useAxios.get(`/api/v1/educations/${educationId}`,
    {}
    ,(res)=> {

        const customData = {
            ...res.data,
            startDateText : useFilters().YYYYMMDD(res.data.startDate,'YYYY월 MM월 DD일'),
            endDateText : useFilters().YYYYMMDD(res.data.endDate,'YYYY월 MM월 DD일'),
            applicationStartDateText : useFilters().YYYYMMDD(res.data.applicationStartDate,'YYYY월 MM월 DD일'),
            applicationEndDateText : useFilters().YYYYMMDD(res.data.applicationEndDate,'YYYY월 MM월 DD일')
            
        }
        
        var template = document.getElementById("educations-template").innerHTML;
        var result = Mustache.render(template, customData);
        document.getElementById("educations-template").innerHTML = result;

      
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
