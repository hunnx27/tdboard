import useAxios from '/assets/js/api/useAxios.js'
import createFileListModule from "/assets/js/file.js"

const fileListModule = createFileListModule()
window.onload = function() {
    // 파일첨부 세팅
    fileListModule.initFileSetting()

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
            const dataTransfer = fileListModule.dataTransfer
            if(dataTransfer.files.length > 0){
                fileUpload()
            }else{
                saveApi()
            }
        }
        
    })
    

}

// 파일 업로드
async function fileUpload(){
    const dataTransfer = fileListModule.dataTransfer
    // FormData 객체 생성
    const formData = new FormData();
    formData.append('uploadType','FACILITY')

    for (const file of dataTransfer.files) {
        if(!file.id){
            formData.append('files', file);
        }
        
    }

    await useAxios.postMultipart(`/api/v1/files`,
    formData
    ,(res)=> {
        const files = []
        for (const file of dataTransfer.files) {
            if(file.id){
                files.push(file.id)
            }
        }
        res.data?.forEach((file)=>{
            files.push(file.id)
        })
        // console.log(files)
        saveApi(files)
    },(err)=> {
        console.log('error',err)
        // alert(err.response.data.message)
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
                
            }else if(input.type === 'file'){
                const dataTransfer = fileListModule.dataTransfer
                if(dataTransfer.files.length === 0){
                    count ++;
                    textfield.setAttribute('data-status', 'error');
                    if(errorField){
                        errorField.innerText = '값을 입력해주세요.'
                    }
                }else {
                    textfield.setAttribute('data-status', 'active');
                    if(errorField){
                        errorField.innerText = ''
                    }
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

async function saveApi(files) {
    const name = document.getElementById('name').value
    const description = $('#description').summernote('code');
   
    await useAxios.post(`/api/v1/facilities`,
    {
        name,
        description,
        // imageUrl:"",
        files: files || []
    }
    ,(res)=> {

        alert('시설이 등록 되었습니다')
        location.href= "/admin/facility-list"
    },(err)=> {
        alert(err.response.data.message)
    })
}