import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGet, getGetIdentifier } from '../get.model';

export type EntityResponseType = HttpResponse<IGet>;
export type EntityArrayResponseType = HttpResponse<IGet[]>;

@Injectable({ providedIn: 'root' })
export class GetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/gets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(get: IGet): Observable<EntityResponseType> {
    return this.http.post<IGet>(this.resourceUrl, get, { observe: 'response' });
  }

  update(get: IGet): Observable<EntityResponseType> {
    return this.http.put<IGet>(`${this.resourceUrl}/${getGetIdentifier(get) as number}`, get, { observe: 'response' });
  }

  partialUpdate(get: IGet): Observable<EntityResponseType> {
    return this.http.patch<IGet>(`${this.resourceUrl}/${getGetIdentifier(get) as number}`, get, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGetToCollectionIfMissing(getCollection: IGet[], ...getsToCheck: (IGet | null | undefined)[]): IGet[] {
    const gets: IGet[] = getsToCheck.filter(isPresent);
    if (gets.length > 0) {
      const getCollectionIdentifiers = getCollection.map(getItem => getGetIdentifier(getItem)!);
      const getsToAdd = gets.filter(getItem => {
        const getIdentifier = getGetIdentifier(getItem);
        if (getIdentifier == null || getCollectionIdentifiers.includes(getIdentifier)) {
          return false;
        }
        getCollectionIdentifiers.push(getIdentifier);
        return true;
      });
      return [...getsToAdd, ...getCollection];
    }
    return getCollection;
  }
}
