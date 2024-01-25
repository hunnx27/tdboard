import createPaginationModule from "/assets/js/pagination.js";
import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

const paginationModule = createPaginationModule();
$(function(){

    elementEvent()

    getBoardApi(1)
 
    paginationModule.setClickPageNumberHandler((pageNumber)=> {
        console.log(`Page number clicked: ${pageNumber}`);
        getBoardApi(pageNumber)
     })

     const popup = document.getElementById('noti-top-hjcho')
     const surveyCreateBtn = document.getElementById('surveyCreateBtn')
     // 생성버튼
     surveyCreateBtn.addEventListener('click', () =>{
        const saveBtn = document.getElementById('saveBtn')
        saveBtn.dataset.value = ''
        saveBtn.dataset.name = ''
        saveBtn.dataset.url = ''
        
        document.getElementById('name').value = '';
        document.getElementById('surveyUrl').value = ''
        document.getElementById('saveBtn').disabled = true
        popup.style.display = 'block'
     })

     
    // 팝업 취소 버튼
    const popupCloseBtn = document.getElementById('popupCloseBtn')
    popupCloseBtn.addEventListener('click', () =>{
        popup.style.display = 'none'
    })

     // 팝업 취소 버튼
     const cancelBtn = document.getElementById('cancelBtn')
     cancelBtn.addEventListener('click', () =>{
        popup.style.display = 'none'
     })

     // 팝업 저장 버튼
     const saveBtn = document.getElementById('saveBtn')
     saveBtn.addEventListener('click', async () =>{
        if(saveBtn.dataset.value){
            modifyApiCall(saveBtn.dataset.value)
        }else {
            saveApiCall()
        }
        
     })

     const name = document.getElementById('name')
     const surveyUrl = document.getElementById('surveyUrl');

     name.addEventListener('input', function() {
        const saveBtn = document.getElementById('saveBtn')
        if(name.value && surveyUrl.value){
            saveBtn.disabled = false
        }else {
            saveBtn.disabled = true
        }
    })
    surveyUrl.addEventListener('input', function() {
        const saveBtn = document.getElementById('saveBtn')
        if(name.value && surveyUrl.value){
            saveBtn.disabled = false
        }else {
            saveBtn.disabled = true
        }
    })
    
})
async function getBoardApi(pageNumber){
    await useAxios.get('/api/v1/surveys',
        {
            page: pageNumber
        }
        ,(res)=> {
        if(res.data.paging.totalElements > 0){
            let firstPostNumber = res.data.paging.totalElements - (pageNumber - 1) * res.data.paging.pageSize;
            res.data.contents.map((data)=>{
               
                data.createdDateText = useFilters().YYYYMMDD(data.updatedAt || data.createdAt)
                data.no = firstPostNumber
                firstPostNumber--;
            })
            handleSetList(pageNumber, res.data)
        }else {
            handleSetList(pageNumber, res.data)
            paginationModule.setPage(1,1,0)
        }
        const tableHeadText = `Total ${res.data.paging.totalElements}건 ${res.data.paging.totalPages} 페이지`
        document.getElementById("tableHeadText").innerHTML = tableHeadText

        elementEvent()
       
        
    },(err)=> {
        console.log('err',err)
    })

}

function elementEvent() {
    const deleteBtn = document.querySelectorAll('.deleteBtn')
    deleteBtn.forEach(function (btns, idx) {
        if(btns){
            btns.addEventListener('click', function(e) {
                // 클릭한 버튼의 data-value 속성 값 가져오기
                const dataValue = this.getAttribute('data-value');
                
                handleDelete(dataValue);
            });
        }
    })

    const modifyBtn = document.querySelectorAll('.modifyBtn')
    const saveBtn = document.getElementById('saveBtn')
    modifyBtn.forEach(function (btns, idx) {
        if(btns){
            btns.addEventListener('click', function(e) {
                // 클릭한 버튼의 data-value 속성 값 가져오기
                const dataValue = this.getAttribute('data-value');
                const dataName = this.getAttribute('data-name');
                const dataUrl = this.getAttribute('data-url');

                saveBtn.dataset.value = dataValue
                document.getElementById('name').value = dataName;
                document.getElementById('surveyUrl').value = dataUrl
                document.getElementById('noti-top-hjcho').style.display = 'block';
                document.getElementById('saveBtn').disabled = false

            });
        }
    })


}

function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("list-body").innerHTML = result;
    
    paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}


async function handleDelete(id){
    if(confirm('설문지를 삭제 하시겠습니까?')){
        await useAxios.delete(`/api/v1/surveys/${id}`,
        {}
            ,(res)=> {
                // console.log(res.data)
                alert('삭제 처리 되었습니다.')
                location.reload()
        },(err)=> {
            alert(err.response.data.message)
        })
    }
    
}

async function saveApiCall() {

    const name = document.getElementById('name').value
    const surveyUrl = document.getElementById('surveyUrl').value

    await useAxios.post(`/api/v1/surveys`,
        {
            name,
            surveyUrl
        }
            ,(res)=> {
                // console.log(res.data)
                alert('저장 되었습니다.')
                location.reload()
        },(err)=> {
            alert(err.response.data.message)
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

    })
    return count
}


async function modifyApiCall(id) {

    const name = document.getElementById('name').value
    const surveyUrl = document.getElementById('surveyUrl').value

    await useAxios.put(`/api/v1/surveys/${id}`,
        {
            name,
            surveyUrl
        }
            ,(res)=> {
                // console.log(res.data)
                alert('수정 되었습니다.')
                location.reload()
        },(err)=> {
            alert(err.response.data.message)
        })
}