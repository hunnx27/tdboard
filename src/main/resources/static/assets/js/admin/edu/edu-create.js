import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function() {

    initElementEvent()

    $('#description').summernote({
        placeholder: '강의내용',
        tabsize: 2,
        height: 120,
        toolbar: [
            ['style', ['style']],
            ['font', ['bold', 'underline', 'clear']],
            ['color', ['color']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['table', ['table']],
            ['insert', ['link', 'picture', 'video']],
            ['view', ['fullscreen', 'codeview', 'help']]
        ]
    });
    $('#summernote').on('summernote.change', function() {
        // 변경된 내용 가져오기
        const content = $(this).summernote('code');
        const descriptionField = document.getElementById('descriptionField')
        const descriptionFieldError = descriptionField.querySelector('.error-text');
        if(content !== '<p><br></p>'){
            descriptionField.setAttribute('data-status', 'active');
            descriptionFieldError.innerText = ''
        }else {
            descriptionField.setAttribute('data-status', 'error');
            descriptionFieldError.innerText = '값을 입력해주세요.'
        }
    });
}

function initElementEvent(){
    $("#startDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const startDateField = document.getElementById('startDateField')
            startDateField.setAttribute('data-status', 'active');
            // validationButtonVisible();
        }
    });
    $("#endDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const endDateField = document.getElementById('endDateField')
            endDateField.setAttribute('data-status', 'active');
            // validationButtonVisible();
        }
    });
    $("#applicationStartDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const applicationStartDateField = document.getElementById('applicationStartDateField')
            applicationStartDateField.setAttribute('data-status', 'active');
            // validationButtonVisible();
        }
    });
    $("#applicationEndDate").datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(dateText, inst) {
            const applicationEndDateField = document.getElementById('applicationEndDateField')
            applicationEndDateField.setAttribute('data-status', 'active');
            // validationButtonVisible();
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
    saveBtn.addEventListener('click', async () =>{
        const result = await checkVaildation()
        if(result === 0){
            saveEduApi()
        }
        
    })
    
    const textfield = document.querySelectorAll('.textfield');
    textfield.forEach(textfield => {
        textfield.addEventListener('input',()=>{
            // validationButtonVisible()
        })
        
    })
}

async function checkVaildation() {
    const saveBtn = document.getElementById('saveBtn')
    const textfield = document.querySelectorAll('.textfield');
    // const userIdCheckBtn = document.getElementById('userIdCheckBtn')
    let count = 0;
    textfield.forEach(textfield => {
        const errorField = textfield.querySelector('.error-text'); 
        const input = textfield.querySelector('input')
        if(input){
            if(input.value && textfield.dataset.status === 'active') {
                textfield.setAttribute('data-status', 'active');
                if(errorField){
                    errorField.innerText = ''
                }
                
            }else {
                textfield.setAttribute('data-status', 'error');
                if(errorField){
                    errorField.innerText = '값을 입력해주세요.'
                }
                count ++;
            }
        } 

        const description = $('#description').summernote('code');
        const descriptionField = document.getElementById('descriptionField')
        const descriptionFieldError = descriptionField.querySelector('.error-text');
        if(description !== '<p><br></p>'){
            descriptionField.setAttribute('data-status', 'active');
            descriptionFieldError.innerText = ''
        }else {
            descriptionField.setAttribute('data-status', 'error');
            descriptionFieldError.innerText = '값을 입력해주세요.'
        }

    })
    return count
}

async function saveEduApi() {
    const name = document.getElementById('name').value
    const location = document.getElementById('location').value
    const description = $('#description').summernote('code');
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