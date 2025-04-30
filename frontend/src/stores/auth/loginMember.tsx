import { createContext, useState, use } from 'react'
import { useRouter } from 'next/navigation'
import { fetchApi } from '@/utils/api';

//이 부분은 나중에 DTO에 맞게 변경할거임
export interface User {
    id?: number;
    nickname: string;
    userName: string;
    phoneNum?: string;
    creationTime: string;
    modificationTime: string;
    academyId?: string;
    academyCode?: string;
    academyName?: string;
    isAdmin?: boolean;
    profileImageUrl?: string;
}

// 백엔드 응답 타입 - MyInfoResponseDto와 일치하도록
type BackendUser = {
    id?: number
    memberId?: number
    nickName?: string
    userName?: string // 사용자 아이디
    phoneNum?: string
    academyId?: string // 학원 ID (백엔드 응답과 일치)
    academyCode?: string // 학원 코드
    academyName?: string // 학원 이름
    profileImageUrl?: string
    creationTime?: string
    modificationTime?: string
    isAdmin?: boolean
    accessToken?: string
    [key: string]: unknown // any 대신 unknown 사용
}

//컨텍스트 전역관리용
export const LoginMemberContext = createContext<{
    loginMember: User
    setLoginMember: (member: BackendUser) => void
    setNoLoginMember: () => void
    isLoginMemberPending: boolean
    isLogin: boolean
    setIsLogin: (value: boolean) => void
    logout: (callback: () => void) => void
    logoutAndHome: () => void
    checkAdminAndRedirect: () => Promise<boolean>
}>({
    loginMember: createEmptyMember(),
    setLoginMember: () => { },
    setNoLoginMember: () => { },
    isLoginMemberPending: true,
    isLogin: false,

    setIsLogin: () => { },

    logout: () => { },
    logoutAndHome: () => { },
    checkAdminAndRedirect: async () => false,
})

//나머지들은 메서드를 블록화
function createEmptyMember(): User {
    return {
        nickname: '',
        userName: '',
        creationTime: '',
        modificationTime: '',
        academyCode: '',
        academyName: '',
        profileImageUrl: '',
    }
}

export function useLoginMember() {
    const router = useRouter()

    const [isLoginMemberPending, setLoginMemberPending] = useState(true)
    const [loginMember, _setLoginMember] = useState<User>(createEmptyMember())
    const [isLogin, setIsLogin] = useState(false); // 👈 추가

    const setNoLoginMember = () => {
        setLoginMemberPending(false)
    }

    const setLoginMember = (member: BackendUser) => {
        // 백엔드 응답 원본 데이터 확인용 로그 추가
        console.group('LoginMember Store - setLoginMember')

        // 액세스 토큰이 있는 로그인 요청인 경우 처리
        if (member.accessToken && member.id) {
            const user: User = {
                id: member.id,
                nickname: member.userName || '',
                userName: member.userName || '',
                creationTime: '',
                modificationTime: '',
            }
            _setLoginMember(user)
            setIsLogin(true)
            setLoginMemberPending(false)
            console.groupEnd()
            return
        }

        const nickname =
            typeof member.nickName === 'string'
                ? member.nickName
                : typeof member.nickname === 'string'
                    ? member.nickname
                    : ''

        const academyCode =
            typeof member.academyCode === 'string'
                ? member.academyCode
                : typeof member.academyId === 'string'
                    ? member.academyId
                    : ''
                    
        // 프로필 이미지 URL 처리 (트림 및 null/undefined 체크)
        let profileImageUrl = '';
        if (member.profileImageUrl) {
            if (typeof member.profileImageUrl === 'string') {
                profileImageUrl = member.profileImageUrl.trim();
                
                // 이미 URL에 쿼리 파라미터가 있는지 확인
                if (!profileImageUrl.includes('?')) {
                    // 캐시 문제를 방지하기 위해 타임스탬프를 URL에 추가하지 않음
                    // 컴포넌트에서 이미지 로드 시 타임스탬프 추가
                }
                
            }
        }

        const user: User = {
            id: member.id || member.memberId,
            nickname: nickname,
            userName: member.userName ?? '',
            phoneNum: member.phoneNum,
            creationTime: member.creationTime || '',
            modificationTime: member.modificationTime || '',
            academyCode: academyCode,
            academyName: member.academyName || '',
            profileImageUrl: profileImageUrl,
        }

        
        _setLoginMember(user)

        const isValidLogin = !!user.userName || !!user.nickname // <- 사용자 확인 가능한 핵심 필드

        setIsLogin(isValidLogin); // 유효한 사용자 정보가 있을 때만 로그인 상태로 설정
        setLoginMemberPending(false)
        console.groupEnd()
    }

    const logout = (callback: () => void) => {
        fetchApi(`/api/v1/auth/logout`, {
            method: 'DELETE',
            credentials: 'include', // 쿠키를 포함하도록 설정
            headers: {
                'Content-Type': 'application/json',
            },
        }).then(() => {
            // 상태 초기화만 수행 (쿠키 삭제는 백엔드에서 처리)
            _setLoginMember(createEmptyMember());
            setIsLogin(false);
            setLoginMemberPending(false);
            
            console.log('로그아웃 완료');
            
            // localStorage에 저장된 학원 정보 삭제
            if (typeof window !== 'undefined') {
                localStorage.removeItem('academyCode');
                localStorage.removeItem('academyName');
            }
            
            callback();
        }).catch(err => {
            console.error('로그아웃 중 오류 발생:', err);
            
            // 오류가 발생해도 상태는 초기화
            _setLoginMember(createEmptyMember());
            setIsLogin(false);
            setLoginMemberPending(false);
            
            if (typeof window !== 'undefined') {
                localStorage.removeItem('academyCode');
                localStorage.removeItem('academyName');
            }
            
            callback();
        });
    };

    const logoutAndHome = () => {
        logout(() => router.replace('/'))
    }

    // 관리자 권한 확인 함수
    const checkAdminAndRedirect = async () => {
        try {
            const response = await fetchApi(`/api/v1/admin/check`, {
                method: 'GET',
            })

            if (!response.ok) {
                return false
            }

            const isAdmin = await response.json()
            return isAdmin === true
        } catch {
            return false
        }
    }

    return {
        loginMember,
        setLoginMember,
        isLoginMemberPending,
        setNoLoginMember,
        isLogin,
        setIsLogin,
        logout,
        logoutAndHome,
        checkAdminAndRedirect,
    }
}

export function useGlobalLoginMember() {
    return use(LoginMemberContext)
}
