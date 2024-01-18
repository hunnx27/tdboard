import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'

$(async function(){
    const noticeId = document.getElementById("noticeId")
    if(noticeId?.value){
        await useAxios.get(`/api/v1/boards/type/notices/${noticeId.value}`,
            {}
            ,(res)=> {
            // console.log('res',res.data)
            const data = {
                createdDateText: useFilters().YYYYMMDD(res.data.updatedAt || res.data.createdAt),
                ...res.data
            }
            var template = document.getElementById("notice-detail-body").innerHTML;
            var result = Mustache.render(template, data);
            document.getElementById("notice-detail-body").innerHTML = result;
            
        },(err)=> {
            console.log('err',err)
            document.getElementById("notice-detail-body").innerHTML = '<div class="detail-head">공지사항 내용이 없습니다.</div>'
        })
    }else {
        location.href="/contents/notice"
    }
    
})