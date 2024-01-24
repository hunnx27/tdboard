import useAxios from '/assets/js/api/useAxios.js'

window.onload = function() {
    
    initElementEvent()

    const id = document.getElementById('facilityId').value
    getDetailId(id)
}


async function getDetailId(id) {
    await useAxios.get(`/api/v1/facilities/${id}`,
    {}
    ,(res)=> {
        const data = res.data
        document.getElementById('name').value = data.name
        $('#description').summernote('code', data.description)

      
    },(err)=> {
        console.log('err',err)
    })
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
    const id = document.getElementById('facilityId').value
    const name = document.getElementById('name').value
    const description = $('#description').summernote('code');
   
    await useAxios.put(`/api/v1/facilities/${id}`,
    {
        name,
        description,
        imageUrl:""
    }
    ,(res)=> {

        alert('시설이 수정 되었습니다')
        location.href= "/admin/facility-list"
    },(err)=> {
        alert(err.response.data.message)
    })
}