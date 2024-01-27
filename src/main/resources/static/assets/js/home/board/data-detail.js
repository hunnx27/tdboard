import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(async function(){
    const dataId = document.getElementById("dataId")
    if(dataId?.value){
        await useAxios.get(`/api/v1/boards/type/datas/${dataId.value}`,
            {}
            ,(res)=> {
            // console.log('res',res.data)
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
            document.getElementById("data-detail-body").innerHTML = '<div class="detail-head">자료실 내용이 없습니다.</div>'
        })
    }else {
        location.href="/contents/data"
    }
    
    const deleteBtn = document.getElementById('deleteBtn')
    if(deleteBtn){
        deleteBtn.addEventListener('click',()=>{
            deleteApi(dataId.value)
        })
    }
    
})

async function deleteApi(dataId){
    if(confirm('글을 삭제하시겠습니까?')){
        await useAxios.delete(`/api/v1/boards/${dataId}`,
        {}
        ,(res)=> {
            alert('글이 삭제 되었습니다')
            location.href= "/contents/data"
        },(err)=> {
            alert(err.response.data.message)
        })
    }
}