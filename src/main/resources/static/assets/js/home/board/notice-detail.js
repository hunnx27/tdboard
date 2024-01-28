import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(async function(){
    const noticeId = document.getElementById("noticeId")
    if(noticeId?.value){
        await useAxios.get(`/api/v1/boards/type/notices/${noticeId.value}`,
            {}
            ,(res)=> {
            document.getElementById("title").innerText = res.data.title
            document.getElementById("createdDateText").innerText = useFilters().YYYYMMDD(res.data.updatedAt || res.data.createdAt)
            document.getElementById("context").innerHTML = res.data.context

            // 파일첨부 리스트
            if(res.data?.files?.length > 0){
                var fileTemplate = document.getElementById("file-template").innerHTML;
                var fileResult = Mustache.render(fileTemplate, {files: res.data?.files});
                document.getElementById("file-list-body").innerHTML = fileResult;
            }
           

        },(err)=> {
            console.log('err',err)
            document.getElementById("notice-detail-body").innerHTML = '<div class="detail-head">공지사항 내용이 없습니다.</div>'
        })
    }else {
        location.href="/contents/notice"
    }
    
    const deleteBtn = document.getElementById('deleteBtn')
    if(deleteBtn){
        deleteBtn.addEventListener('click',()=>{
            deleteApi(noticeId.value)
        })
    }
    
})

async function deleteApi(noticeId){
    if(confirm('글을 삭제하시겠습니까?')){
        await useAxios.delete(`/api/v1/boards/${noticeId}`,
        {}
        ,(res)=> {
            alert('글이 삭제 되었습니다')
            location.href= "/contents/notice"
        },(err)=> {
            alert(err.response.data.message)
        })
    }
}