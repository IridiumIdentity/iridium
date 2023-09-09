export class CookieService {
    public getCookie(name: string) {
        const cookieArray: Array<string> = document.cookie.split(';');
        const cookieArrayLength: number = cookieArray.length;
        const cookieName = `${name}=`;

        for (let i: number = 0; i < cookieArrayLength; i += 1) {
            let c = cookieArray[i].replace(/^\s+/g, '');
            if (c.indexOf(cookieName) == 0) {
                return c.substring(cookieName.length, c.length);
            }
        }
        return '';
    }

    public deleteCookie(name: string) {
        this.setCookie(name, '', -1);
    }

    public setCookie(name: string, value: string, expireDays: number, path: string = '') {
        let d:Date = new Date();
        d.setTime(d.getTime() + expireDays * 24 * 60 * 60 * 1000);
        let expires:string = `expires=${d.toUTCString()}`;
        let cpath:string = path ? `; path=${path}` : '';
        document.cookie = `${name}=${value}; ${expires}${cpath}`;
    }
}
