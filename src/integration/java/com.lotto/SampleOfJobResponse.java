package com.lotto;

public interface SampleOfJobResponse {

    default String bodyWithZeroOffersJson() {
        return "[]";
    }

    default String bodyWithOneOfferJson() {
        return
                """
                        [
                            {
                                "title": "Junior Java Developer NOWA",
                                    "company": "Netcompany Poland Sp. z o.o.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"
                            }
                        [
                        """.trim();
    }

    default String bodyWithTwoOffersJson() {
        return
                """
                        [
                            {
                                "title": "Junior Java Developer NOWA",
                                    "company": "Netcompany Poland Sp. z o.o.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"
                            },
                            {
                                "title": "Praktykant Java Developer NOWA",
                                    "company": "Pentacomp Systemy Informatyczne S.A.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/praktykant-java-developer-pentacomp-systemy-informatyczne-warszawa"
                            }
                        [
                        """.trim();
    }

    default String bodyWithFourOffersJson() {
        return
                """
                        [
                            {
                                "title": "Junior Java Developer NOWA",
                                    "company": "Netcompany Poland Sp. z o.o.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"
                            },
                            {
                                "title": "Praktykant Java Developer NOWA",
                                    "company": "Pentacomp Systemy Informatyczne S.A.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/praktykant-java-developer-pentacomp-systemy-informatyczne-warszawa"
                            },
                            {
                                "title": "Junior Java Backend Developer",
                                    "company": "VHV Informatyka Sp. z o.o.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-backend-developer-vhv-informatyka-warszawa"
                            },
                            {
                                "title": "Software Developer",
                                    "company": "BrainworkZ",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/software-developer-brainworkz-warsaw"
                            }
                        [
                        """.trim();
    }

    default String bodyWithManyOffersJson() {
        return
                """
                        [
                            {
                                "title": "Junior Java Developer NOWA",
                                    "company": "Netcompany Poland Sp. z o.o.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-netcompany-poland-warsaw-3"
                            },
                            {
                                "title": "Praktykant Java Developer NOWA",
                                    "company": "Pentacomp Systemy Informatyczne S.A.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/praktykant-java-developer-pentacomp-systemy-informatyczne-warszawa"
                            },
                            {
                                "title": "Junior Java Backend Developer",
                                    "company": "VHV Informatyka Sp. z o.o.",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-backend-developer-vhv-informatyka-warszawa"
                            },
                            {
                                "title": "Software Developer",
                                    "company": "BrainworkZ",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/software-developer-brainworkz-warsaw"
                            },
                            {
                                "title": "AI Engineer",
                                    "company": "Strategy",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/ai-engineer-strategy-warsaw"
                            },
                            {
                                "title": "Salesforce Developer - Force Academy",
                                    "company": "Britenet",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/salesforce-developer-force-academy-britenet-lublin-4"
                            },
                            {
                                "title": "Java Software Engineer NOWA",
                                    "company": "VISA",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/java-software-engineer-visa-warszawa"
                            },
                            {
                                "title": "Junior Software Engineer - Java NOWA",
                                    "company": "VISA",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-software-engineer-java-visa-warszawa"
                            },
                            {
                                "title": "Intern - Java Developer with German NOWA",
                                    "company": "Capgemini Polska Sp. z o.o.",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/intern-java-developer-with-german-capgemini-polska-wroclaw"
                            },
                            {
                                "title": "Junior Java Developer with German NOWA",
                                    "company": "Capgemini Polska Sp. z o.o.",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-with-german-capgemini-polska-gdansk"
                            },
                            {
                                "title": "Junior Java Developer",
                                    "company": "Bersi Sp. z o.o.",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-java-developer-bersi-bydgoszcz-3"
                            },
                            {
                                "title": "Junior .NET Backend Software Engineer NOWA",
                                    "company": "VISA",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-net-backend-software-engineer-visa-warszawa-1"
                            },
                            {
                                "title": "Database Staff Software Engineer NOWA",
                                    "company": "VISA",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/database-staff-software-engineer-visa-warszawa"
                            },
                            {
                                "title": "Junior Android Developer 63428 NOWA",
                                    "company": "Indeema Software",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/junior-android-developer-63428-indeema-software-wroclaw-1"
                            },
                            {
                                "title": "Java Developer (with German) NOWA",
                                    "company": "Capgemini Polska Sp. z o.o.",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/java-developer-with-german-capgemini-polska-krakow-kavvfy59"
                            },
                            {
                                "title": "Java Developer",
                                    "company": "Scalo",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/java-developer-scalo-warszawa-qggrh5px"
                            },
                            {
                                "title": "Java Developer NOWA",
                                    "company": "ASTEK Polska",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/java-developer-astek-polska-lublin-txltjbtr"
                            },
                            {
                                "title": "Fullstack Java Developer NOWA",
                                    "company": "Matrix Global Services",
                                    "salary": null,
                                    "offerUrl": "https://nofluffjobs.com/pl/job/fullstack-java-developer-matrix-global-services-remote-i3ym3lsw"
                            },
                            {
                                "title": "Cloud Data Engineer (Snowflake)",
                                    "company": "Capgemini Polska Sp. z o.o.",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/cloud-data-engineer-snowflake-capgemini-polska-krakow-3bq8x5yw"
                            },
                            {
                                "title": "Java Developer (with German) NOWA",
                                    "company": "Capgemini Polska Sp. z o.o.",
                                    "salary": "Sprawdź zarobki",
                                    "offerUrl": "https://nofluffjobs.com/pl/job/java-developer-with-german-capgemini-polska-wroclaw-wz2dwg2i"
                            }
                        ]
                        """.trim();
    }

}
