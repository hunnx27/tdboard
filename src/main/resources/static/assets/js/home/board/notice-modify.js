import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

window.onload = function () {

    getBoardApi()
  
    $('#description').summernote({
        placeholder: '내용',
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
               validationButtonVisible()
            }
        }
    });

    const titleField = document.getElementById('titleField')
    const title = document.getElementById('title')
    const titleFieldError = titleField.querySelector('.error-text');
    title.addEventListener('input', ()=>{
        if(title.value){
            titleField.setAttribute('data-status', 'active');
            titleFieldError.innerText = ''
        }else{
            titleField.setAttribute('data-status', 'error');
            titleFieldError.innerText = '제목을 입력해주세요.'
        }
        validationButtonVisible()
    })

    const saveBtn = document.getElementById('saveBtn');
    saveBtn.addEventListener('click', function(){
        if(title.value )
        saveApi()
    })

    
}
function validationButtonVisible(){
    const title = document.getElementById('title')
    const description = $('#description').summernote('code');
    const saveBtn = document.getElementById('saveBtn');
    if(title.value && description){
        saveBtn.disabled = false
    }else{
        saveBtn.disabled = true
    }
}

async function getBoardApi(){
    const noticeId = document.getElementById("noticeId")
    if(noticeId?.value){
        await useAxios.get(`/api/v1/boards/type/notices/${noticeId.value}`,
            {}
            ,(res)=> {
                document.getElementById("title").value = res.data.title
                $('#description').summernote('code',res.data.context)
                
            
        },(err)=> {
            console.log('err',err)
            document.getElementById("notice-detail-body").innerHTML = '<div class="detail-head">공지사항 내용이 없습니다.</div>'
        })
    }else {
        alert(err.response.data.message)
    }
}

async function saveApi() {
    const noticeId = document.getElementById("noticeId").value
    const title = document.getElementById("title").value
    const description = $('#description').summernote('code');

    await useAxios.put(`/api/v1/boards/${noticeId}`,
            {
                boardType: 'NOTICE',
                title,
                context: description
            }
            ,(res)=> {
                alert('글이 저장 되었습니다')
                location.href= "/contents/notice"
            },(err)=> {
                alert(err.response.data.message)
            })
 

}