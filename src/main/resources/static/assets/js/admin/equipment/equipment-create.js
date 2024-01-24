import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'
import createPaginationModule from "/assets/js/pagination.js";

const paginationModule = createPaginationModule();

window.onload = function() {
    
    initElementEvent()

     //popup list
    getBoardApi(1)

    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getBoardApi(pageNumber)
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
    
    const popup = document.getElementById('noti-top-hjcho')
    const popupCloseBtn = document.getElementById('popupCloseBtn')
    const facilityListBtn = document.getElementById('facilityListBtn')
    facilityListBtn.addEventListener('click',(e) =>{
        popup.style.display = 'block'
    })
   
    popupCloseBtn.addEventListener('click',(e) =>{
        popup.style.display = 'none'
    })

    

}

async function getBoardApi(pageNumber){
    await useAxios.get('/api/v1/facilities',
        {page: pageNumber}
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{
                data.no = firstPostNumber
                firstPostNumber--;
            })
            handleSetList(pageNumber, res.data)

            const popup = document.getElementById('noti-top-hjcho')
            const popupItem = document.querySelectorAll('.popup-item')
            const location = document.getElementById('location')
            popupItem.forEach(element =>{
                element.addEventListener('click',(e) =>{
                    
                    location.dataset.value = element.dataset.value
                    location.value = element.dataset.title
                    const locationField = document.getElementById('locationField')
                    locationField.dataset.status='active'
                    locationField.querySelector('.error-text').innerText = ''


                    popup.style.display = 'none'
                })
            })
        }else {
            paginationModule.setPage(1,1,0)
        }

        
    },(err)=> {
        console.log('err',err)
    })

}

function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("list-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
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
    const locationValue = document.getElementById('location').value
    const facilityId = document.getElementById('location').dataset.value
    const description = $('#description').summernote('code');
   
    await useAxios.postMultipart(`/api/v1/equipments`,
    {
        name,
        facilityId,
        location:locationValue,
        description,
        imageUrl:""
    }
    ,(res)=> {

        alert('장비가 생성 되었습니다')
        location.href= "/admin/equipment-list"
    },(err)=> {
        alert(err.response.data.message)
    })
}