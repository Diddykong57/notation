import * as dayjs from 'dayjs';
import { IGet } from 'app/entities/get/get.model';
import { ISubject } from 'app/entities/subject/subject.model';
import { TypeAssessment } from 'app/entities/enumerations/type-assessment.model';

export interface IAssessment {
  id?: number;
  date?: dayjs.Dayjs | null;
  coefCont?: number | null;
  type?: TypeAssessment | null;
  gets?: IGet[] | null;
  subject?: ISubject;
}

export class Assessment implements IAssessment {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public coefCont?: number | null,
    public type?: TypeAssessment | null,
    public gets?: IGet[] | null,
    public subject?: ISubject
  ) {}
}

export function getAssessmentIdentifier(assessment: IAssessment): number | undefined {
  return assessment.id;
}
