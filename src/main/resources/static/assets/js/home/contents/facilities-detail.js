import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'



window.onload = function () {
    const facilitiesId = document.getElementById('facilitiesId').value

    getEquipmentsApi(facilitiesId)

    initElementSetting()
};

async function getEquipmentsApi(id){
    await useAxios.get(`/api/v1/facilities/${id}`,
        {page: 1}
        ,(res)=> {
        // console.log('res',res.data)
        if(res.data){
            var template = document.getElementById("facilities-detail-body").innerHTML;
            var result = Mustache.render(template, res.data);
            document.getElementById("facilities-detail-body").innerHTML = result;
            
            const applicationBtn = document.getElementById("applicationBtn")
            applicationBtn.addEventListener('click', () =>{
                //api
                alert('신청 완료')
            })
        }
    },(err)=> {
        console.log('err',err)
    })

}

function initElementSetting(){
    // 현재 년도 가져오기
    const currentYear = new Date().getFullYear();

    // select 요소 가져오기
    const yearSelect = document.getElementById('yearSelect');
    const monthSelect = document.getElementById('monthSelect');
    const daySelect = document.getElementById('daySelect');

    // 옵션 추가: 현재 년도부터 현재 년도 + 2년까지
    for (let year = currentYear; year <= currentYear + 2; year++) {
        const option = document.createElement('option');
        option.value = year;
        option.text = year;
        yearSelect.appendChild(option);
    }
    // 옵션 추가: 1월부터 12월까지
    for (let month = 1; month <= 12; month++) {
        const option = document.createElement('option');
        option.value = month;
        option.text = month;
        monthSelect.appendChild(option);
    }

    // 옵션 추가: 1일부터 31일까지
    for (let day = 1; day <= 31; day++) {
        const option = document.createElement('option');
        option.value = day;
        option.text = day;
        daySelect.appendChild(option);
    }
}