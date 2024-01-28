import useAxios from '/assets/js/api/useAxios.js'
import useFilters from '/assets/js/useFilters.js'


const phoneRegex = /^01[0-9]-\d{3,4}-\d{4}$/;
let selectedDate = '';
let selectedStartHour = 0;
let selectedEndHour = 0;

window.onload = function () {

    // init
    const facilitiesId = document.getElementById('facilitiesId').value
    getFacilitiesApi(facilitiesId)
    getEquipmentsInRacility(1) // 시설에 포함되어있는 장비
    const today = new Date();
    const todayText = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
    selectedDate = todayText
    getAvailiableTime(facilitiesId, todayText); // 학습가능 시간 조회
    // event
    $(".datepicker").datepicker({
        dateFormat: 'yy-mm-dd',
        monthNames: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        monthNamesShort: [ "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" ],
        dayNamesMin: [ "일", "월", "화", "수", "목", "금", "토" ],
        minDate: 0,
        onSelect: function(dateText, inst) {
            selectedDate = dateText;
            getAvailiableTime(facilitiesId, dateText)
        }
    }).datepicker("setDate", new Date());
};

async function getFacilitiesApi(id){
    await useAxios.get(`/api/v1/facilities/${id}`,
        {page: 1}
        ,(res)=> {
        console.log('res',res.data)
        if(res.data){
            const facilitiesImg = document.getElementById('facilitiesImg')
            facilitiesImg.setAttribute('src',res.data.imageUrl)
            const facilitiesTitle = document.getElementById('facilitiesTitle')
            facilitiesTitle.innerText = res.data.name
            
            const applicationBtn = document.getElementById("applicationBtn")
            applicationBtn.addEventListener('click', () =>{
                //api
                if(validationBooking(id)){
                    save(id);
                }
            })
        }
    },(err)=> {
        console.log('err',err)
    })

}

let bookedList = []; // 예약된 리스트(예약불가항)
const DEFAULT_HOUR_SELECT="시간"
async function getAvailiableTime(id, targetDate){
    const TYPE = 'FACILITY'
    // 초기화
    const select1 = document.querySelector('#hourSelect1.select');
    const select2 = document.querySelector('#hourSelect2.select');
    const select1Value = select1.querySelector('.select-btn .select-value');
    const select2Value = select2.querySelector('.select-btn .select-value');
    select1.setAttribute('data-status', '');
    select2.setAttribute('data-status', '');
    select1Value.innerHTML = DEFAULT_HOUR_SELECT;
    select2Value.innerHTML = DEFAULT_HOUR_SELECT;
    bookedList = [];
    selectedStartHour = 0;
    selectedEndHour = 0;

    await useAxios.get(`/api/v1/bookings/bookingType/${TYPE}/target/${id}/available-times`,
        {targetDate: targetDate}
        ,(res)=> {
            console.log('res',res.data)
            if(res.data){
                bookedList = res.data;
                const hourOptionData = []
                for (let hour = 9; hour <= 24; hour++) {
                    hourOptionData.push({
                        'value' : hour,
                        'text' : hour,
                        'full' : bookedList.includes(hour)
                    });
                }
                var template = document.getElementById("select-option-template").innerHTML;
                var result = Mustache.render(template, {options: hourOptionData});
                document.getElementById("hourSelectOption1").innerHTML = result;
                document.getElementById("hourSelectOption2").innerHTML = result;

                // 예약된 옵션 선택시 이벤트 생성
                const startOption = document.querySelectorAll('#hourSelect1.select .option-box .option');
                startOption.forEach((option)=>{
                    option.addEventListener('click',()=>{
                        console.log('aleady booked.. this time 1');
                        selectedStartHour = Number(option.querySelector('span').innerText)
                    })
                });
                const endOption = document.querySelectorAll('#hourSelect2.select .option-box .option');
                endOption.forEach((option)=>{
                    option.addEventListener('click',()=>{
                        selectedEndHour = Number(option.querySelector('span').innerText);
                    })
                });
            }
        },(err)=> {
            console.log('err',err)
        })
}

async function save(id){
    await useAxios.post(`/api/v1/users/me/bookings`,
        {
            "bookingType":"FACILITY", //EDUCATION/FACILITY/EQUIPMENT
            "startAt":`${selectedDate} ${String(selectedStartHour).padStart(2, '0')}:00:00`,
            "endAt":`${selectedDate} ${String(selectedEndHour).padStart(2, '0')}:00:00`,
            "reqPhone":document.querySelector("#req-phone").value,
            "facilityId":id
        }
        ,(res)=> {
            alert("예약 요청이 완료되었습니다.(주의. 관리자 승인이 완료되어야 이용이 가능합니다.)");
            location.reload(true);
        }
        ,(err)=>{
            console.log('err',err)
        }
    )
}
function validationBooking(){
    // 일자 체크
    const today = new Date();
    const todayText = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
    const todayHour = today.getHours();
    let bookingHours = [];
    for(var hour = Number(selectedStartHour); hour<Number(selectedEndHour); hour++){
        bookingHours.push(hour);
    }
    // 예약된 리스트 체크
    debugger;
    let isBooking = false;
    bookingHours.forEach((hour)=>{
        if(bookedList.includes(hour)){
            isBooking = true;
            return true;
        }
    });
    if(isBooking){
        alert("이미 예약된 시간입니다. 다른 시간을 선택해주세요.");
        return false;
    }
    // 오늘 일자인 경우 현재 시간 이후만 선택됨
    if(todayText == selectedDate){
        if(todayHour >= selectedStartHour || todayHour >= selectedEndHour){
            alert("현재 시간 이하는 예약이 불가합니다. 다른 시간을 선택해주세요.");
            return false;
        }
    }
    // 시작 종료시간 체크
    if(selectedStartHour >= selectedEndHour){
        alert("시작시간보다 종료시간이 작거나 같습니다. 종료시간을 시작시간보다 크게 선택해주세요.");
        return false;
    }

    const reqPhone = document.querySelector("#req-phone").value;
    if(reqPhone== undefined || reqPhone==''){
        alert("요청 전화번호를 입력하세요.");
        return false;
    }
    if (!phoneRegex.test(reqPhone)) {
        alert('휴대전화번호 형식에 맞게 입력해주세요(010-0000-0000).')
        return false;
    }

    return true;
}


async function getEquipmentsInRacility(pageNumber){
    const id = document.getElementById('facilitiesId').value
    await useAxios.get(`/api/v1/equipments/in-facility/${id}`,
    {
        page: pageNumber,
        size: 100
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
            // paginationModule.setPage(1,1,0)
        }

      
    },(err)=> {
        console.log('err',err)
    })
}

function handleSetList(pageNumber, data){
    var template = document.getElementById("table-template").innerHTML;
    var result = Mustache.render(template, data);
    document.getElementById("list-body").innerHTML = result;
    
    // paginationModule.setPage(pageNumber,data.paging.pageSize,data.paging.totalElements)
}