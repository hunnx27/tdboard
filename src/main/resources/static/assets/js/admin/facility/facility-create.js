import useAxios from '/assets/js/api/useAxios.js'

window.onload = function() {
    
    initElementEvent()

}


function initElementEvent(){
    $('#description').summernote({
        placeholder: '장비내용',
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
        ],
        callbacks: {
            onChange: function(contents, $editable) {
                // 내용이 변경될 때의 콜백
                // 변경된 내용 가져오기
                const descriptionField = document.getElementById('descriptionField')
                const descriptionFieldError = descriptionField.querySelector('.error-text');
                if(contents !== '<p><br></p>'){
                    descriptionField.setAttribute('data-status', 'active');
                    descriptionFieldError.innerText = ''
                }else {
                    descriptionField.setAttribute('data-status', 'error');
                    descriptionFieldError.innerText = '값을 입력해주세요.'
                }
            }
        }
    });


   
    const saveBtn = document.getElementById('saveBtn');
    saveBtn.addEventListener('click', async () =>{
        const result = await checkVaildation()
        if(result === 0){
            saveApi()
        }
        
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

async function saveApi() {
    const name = document.getElementById('name').value
    const description = $('#description').summernote('code');
   
    await useAxios.postMultipart(`/api/v1/facilities`,
    {
        name,
        description,
        imageUrl:""
    }
    ,(res)=> {

        alert('시설이 등록 되었습니다')
        location.href= "/admin/facility-list"
    },(err)=> {
        alert(err.response.data.message)
    })
}