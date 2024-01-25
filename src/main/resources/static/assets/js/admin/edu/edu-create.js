import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function() {

initElementEvent()


}

function initElementEvent(){
    $("#startDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const startDateField = document.getElementById('startDateField')
            startDateField.setAttribute('data-status', 'active');
            validationButtonVisible();
        }
    });
    $("#endDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const endDateField = document.getElementById('endDateField')
            endDateField.setAttribute('data-status', 'active');
            validationButtonVisible();
        }
    });
    $("#applicationStartDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const applicationStartDateField = document.getElementById('applicationStartDateField')
            applicationStartDateField.setAttribute('data-status', 'active');
            validationButtonVisible();
        }
    });
    $("#applicationEndDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const applicationEndDateField = document.getElementById('applicationEndDateField')
            applicationEndDateField.setAttribute('data-status', 'active');
            validationButtonVisible();
        }
    });

    const capacityField = document.getElementById('capacityField');

    // "option" 클래스를 가진 버튼 요소들을 선택
    const optionButtons = document.querySelectorAll('.option');

    // 각 버튼에 클릭 이벤트 리스너 추가
    optionButtons.forEach(button => {
        button.addEventListener('click', () => {
            // 클릭된 버튼의 data-option 값을 가져와서 selectJoinPathField의 data-value에 설정
            const dataOptionValue = button.dataset.option;
            capacityField.dataset.value = dataOptionValue;
        });
    });

    const saveBtn = document.getElementById('saveBtn');
    saveBtn.addEventListener('click', () =>{
        saveEduApi()
    })
    
    const textfield = document.querySelectorAll('.textfield');
    textfield.forEach(textfield => {
        textfield.addEventListener('input',()=>{
            // validationButtonVisible()
        })
        
    })
}

async function validationButtonVisible() {
    const saveBtn = document.getElementById('saveBtn')
    const textfield = document.querySelectorAll('.textfield');
    // const userIdCheckBtn = document.getElementById('userIdCheckBtn')
    let count = 0;
    textfield.forEach(textfield => {
        const input = textfield.querySelector('input')
        if(input){
            if(input.value && textfield.dataset.status !== 'active') {
                count ++;
            }
        } 
        const description = textfield.querySelector('textarea')
        if(description){
            if(description.value && textfield.dataset.status !== 'active') {
                count ++;
            }
        } 
        
        
    })
    
    console.log(count)
        if(count > 0){
            saveBtn.disabled = true
        }else {
            saveBtn.disabled = false
        }
}


function checkVaildation() {
   
    const textfield = document.querySelectorAll('.textfield');
    // const userIdCheckBtn = document.getElementById('userIdCheckBtn')
    let count = 0;
    textfield.forEach(textfield => {
        const input = textfield.querySelector('input')
        const errorText = textfield.querySelector('.error-text')
        if(input){
            if(input.value && textfield.dataset.status !== 'active') {
                textfield.dataset.status = 'error'
                errorText.innerText = "값을 입력해주세요."
            }else {
                textfield.dataset.status = 'default'
                errorText.innerText = ""
            }
        } 
        const description = textfield.querySelector('textarea')
        if(description){
            if(description.value && textfield.dataset.status !== 'active') {
                count ++;
            }
        } 
        
        
    })
    
}
//"facilityId":2,
// "name":"교육2",
// "location":"시설2 1층",
// "description":"교육을 등록해 봤습니다.2",
// "imageUrl":"/data/hiroo/img.jpg",
// "startDate":"2024-01-01",
// "endDate":"2024-01-03",
// "applicationStartDate":"2023-12-25",
// "applicationEndDate":"2023-12-31",
// "manager":"유관순교수",
// "capacity":31,
// "useYn":"Y",
// "delYn":"N"
// }
async function saveEduApi() {
    const name = document.getElementById('name').value
    const location = document.getElementById('location').value
    const description = document.getElementById('description').value
    const startDate = document.getElementById('startDate').value
    const endDate = document.getElementById('endDate').value
    const applicationStartDate = document.getElementById('applicationStartDate').value
    const applicationEndDate = document.getElementById('applicationEndDate').value
    const manager = document.getElementById('manager').value
    const capacity = document.getElementById('capacityField').dataset.value
    const formData = new FormData()
    formData.append('name',name)
    formData.append('location',location)
    formData.append('description',description)
    formData.append('startDate',startDate)
    formData.append('endDate',endDate)
    formData.append('applicationStartDate',applicationStartDate)
    formData.append('applicationEndDate',applicationEndDate)
    formData.append('manager',manager)
    formData.append('capacity',capacity)
    formData.append('imageUrl',"")

    // console.log(formData)
    for (let [key, value] of formData.entries()) {
        
        console.log(`${key}: ${value}`);
    }

    await useAxios.postMultipart(`/api/v1/educations`,
    {
        name,
        location,
        description,
        startDate,
        endDate,
        applicationStartDate,
        applicationEndDate,
        manager,
        capacity,
        imageUrl:""
    }
    ,(res)=> {

        alert('교육 생성이 되었습니다')
        // location.href= "/admin/edu-list"
    },(err)=> {
        alert(err.response.data.message)
    })
}