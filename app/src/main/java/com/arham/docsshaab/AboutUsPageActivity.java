package com.arham.docsshaab;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsPageActivity extends AppCompatActivity {

    final String DESC = "Docsaab is the best android app for quick, safe, economic and effective treatment  naturally. Using this app you can not only cure diseases at home but also get the information of different diseases like symptoms and causes. Learn to use natural herbs as medicine alternatives.<br>" +
            "<br>" +
            "Home treatment and natural cure for stomach, hair, skin, respiratory, circulatory, head, jaw and teeth, bone/joint, eye, and many other diseases.<br>" +
            "<br>" +
            "Features:<br>" +
            "Home Treatment (Natural Cure) <br>" +
            "Diseases by categories<br>" +
            "Add bookmarks<br>" +
            "share the app <br>" +
            "Suggest diseases for natural treatment<br>" +
            "Share treatment to your friends and family<br>" +
            "Offline<br><br>" +
            "<b> TERMS OF USE </b><br>" +
            "Welcome to the website or mobile application of Docsaab The following terms and conditions, together with any documents they incorporate by reference, including without limitation the Privacy Policy (collectively, these “Terms of Use”), govern your access to and use of Docsaab suite of mobile applications, including without limitation . When using the Apps, you may be presented with additional Terms of Use, specific to such App, such additional terms (if any) shall be incorporated into these Terms of Use and, together with these Terms of Use, govern your use of such App.<br>" +
            "<br>" +
            "Please read the Terms of Use carefully before you start to use the Services. BY ACCESSING, BROWSING OR USING THE SERVICES (INCLUDING YOUR SUBMISSION OF INFORMATION TO THE WEBSITE), YOU ACKNOWLEDGE THAT YOU HAVE READ, UNDERSTOOD, AND AGREED TO BE BOUND BY THESE TERMS OF USE, INCLUDING THE PRIVACY POLICY (WHICH IS INCORPORATED HEREIN BY REFERENCE), AND TO COMPLY WITH ALL APPLICABLE LAWS AND REGULATIONS. You agree that the Terms of Use, combined with your act of using the Services, has the same legal force and effect as a written contract with your written signature and satisfy any laws that require a writing or signature. You further agree that you shall not challenge the validity, enforceability, or admissibility of the Terms of Use on the grounds that it was electronically transmitted or authorized.<br>" +
            "<br>" +
            "The Services is offered and available to users who are 13 years of age or older. By using the Services, you represent and warrant that you meet the foregoing eligibility requirement. If you do not meet this requirement, you must not access or use the Services.<br>" +
            "<br>" +
            "PLEASE BE AWARE THAT THESE TERMS INCLUDE LIMITATIONS ON THE LIABILITY OF DOCSAAB AND OUR OBLIGATIONS RELATING TO THE SERVICES, CERTAIN CONDITIONS WITH RESPECT TO JURISDICTION, AND CERTAIN EXCLUSIONS OF Docsaab's RESPONSIBILITY.<br>" +
            "<br>" +
            "THE SERVICES DO NOT PROVIDE MEDICAL ADVICE.<br><br>" +
            "<b>CONTENTS</b><br>" +
            "<br>" +
            "The Contents of the Docsaab Services, such as text, graphics, images, information obtained from INTERNET Services (\"Content\") are for informational purposes only. The content is not intended to be a substitute for professional medical advice, diagnosis, or treatment. Always seek the advice of your physician or other qualified health provider with any question you may have regarding a medical condition.<br>" +
            "<br>" +
            "If you think you may have a medical emergency, call your doctor or 911 immediately. Docsaab does not recommend or endorse any specific tests, physician, products, procedures, opinions, or other information that may be mentioned on the Services. Reliance on any information provided by Docsaab employees, others appearing on the Services at the invitation of Docsaab , or other visitors to the Services is solely at your own risk. The Services may contain health-or-medical- related materials that are sexually explicit. If you find these materials offensive, you may not want to use our Site.<br>" +
            "<br>" +
            "Unless otherwise noted, all Contents on the Services, whether publicly posted or privately transmitted, as well as all derivative works are property owned, controlled, licensed or used with permission by Docsaab , and/or its parents, subsidiaries and affiliates or other parties that have licensed to or otherwise permitted their material to be used by Docsaab . The Services as a whole and their Contents are protected by copyright, trademark, trade dress and other laws and all worldwide right, title and interest in and to the Services and its Contents are owned by Docsaab or used with permission. <br>" +
            "<br>" +
            "Docsaab , the Docsaab logos, and all other trademarks appearing on the Services are trademarks of Docsaab or are licensed or used with permission of the owner by Docsaab . You agree not to display or use such trademarks without Docsab prior written permission. Docsaab disclaims any proprietary interest in trademarks, service marks, logos, slogans, domain names and trade names other than its own.<br>" +
            "<br>" +
            "The Contents of the Services, and the Services as a whole, are intended solely for personal, non-commercial use by the users of the Services and may not be used except as permitted in these Terms of Use. You may share content from the Site with the use of the social media links (i.e. “Share:” “Pin It” and “Tweet”) provided on our Services.  You may also share content from the Services via email through the use of our “Share” link.  You may download or copy the Contents and other downloadable materials displayed on the Services for your personal use only. No right, title or interest in any downloaded materials or software is transferred to you as a result of any such downloading, sharing or copying. Except as noted above, you may not reproduce, republish, publish, upload, post, transmit, distribute (including by email or other electronic means), publicly display, modify, create derivative works from, sell or participate in any sale of, or exploit in any way, in whole or in part, any of the Contents, the Services, or any related software without the prior written consent of Docsaab  or the owner of such material. Nothing contained on the Services grants or should be construed as granting, any license or right to use, implied or otherwise, any trademarks, trade names, service marks, trade dress, copyrighted or other proprietary material displayed on this Services without the prior written consent of Docsaab or the owner of such material. All rights not expressly granted herein by Docsaab to you are reserved by Docsaab  and/or its licensors. Third-party trade names, product names and logos, contained in these Websites may be the trademarks or registered trademarks of their respective owners.<br>" +
            "<br>" +
            "The information presented on or through the Services is made available solely for general information purposes. We may update the content on this Services from time to time, but the content is not necessarily complete or up-to-date. Any of the material on the Services may be out of date at any given time, and we are under no obligation to update such material.<br><br>" +
            "<b>ACCEPTABLE USE</b><br>" +
            "<br>" +
            "You are prohibited from violating or attempting to violate the security measures on the Services, including, without limitation:<br>" +
            "<br>" +
            " 1.  Using a false password or one belonging to another user or accessing data not intended for the user or logging into a server or account which such user is not authorized to access;<br>" +
            " 2.  Disclosing a password or permitting a third party to use a password or failing to notify us if a password is compromised;<br>" +
            " 3.  Attempting to probe, scan or test the vulnerability of the system or network or to breach security or authentication measures without proper authorization;<br>" +
            " 4.  Attempting to interfere with service to any user, host or network, including, without limitation, via means of overloading, \"flooding\", \"mail bombing\" or \"crashing\";<br>" +
            " 5.  Sending unsolicited e-mail or commercial electronic messages, including promotions and/or advertising of products or services;<br>" +
            " 6.  Forging any TCP/IP packet header or any part of the header information in any e-mail or newsgroup posting; or<br>" +
            " 7.  Hijacking all or any part of the Services content, deleting or changing any Services content, deploying pop-up messages or advertising, running or displaying this Services or any Services content in frames or through similar means on another website, or linking to the Services or any page within the Services, without our specific written permission.<br>" +
            "<br><br>" +
            "<b>GENERAL RULES</b><br>" +
            "<br>" +
            "You agree to use the Services only for lawful purposes and only for your own personal, non-commercial use. You may not use the Services to transmit, post, download, distribute, copy, display publicly, store or destroy material (a) in violation of any applicable law or regulation, (b) in a manner that will infringe the copyright, patent, trademark, trade secret or other intellectual property rights of others or violate the privacy or publicity or other personal rights of others, or (c) that is libelous, obscene, offensive, threatening, defamatory, abusive or hateful.<br>" +
            "<br>" +
            "We have absolute discretion to determine if any use violates these rules, and to act as we deem appropriate in the event of any violation. Violations of system or network security may result in civil or criminal liability. We will investigate occurrences which may involve such violations and may involve, cooperate with, and make disclosures to, law enforcement authorities in identifying and prosecuting users who are involved in such violations.<br><br>" +
            "<b>LIABILITY OF DOCSAAB</b><br>" +
            "<br>" +
            "BECAUSE SOME JURISDICTIONS DO NOT ALLOW EXCLUSIONS OF IMPLIED WARRANTIES, LIMITATIONS ON HOW LONG AN IMPLIED WARRANTY LASTS, OR THE EXCLUSION OR LIMITATION OF LIABILITY FOR CONSEQUENTIAL OR INCIDENTAL DAMAGES, THE BELOW LIMITATIONS MAY NOT APPLY TO YOU.<br>" +
            "<br>" +
            "The use of the Docsaab Services and the Content is at your own risk. When using the Docsaab Services, information will be transmitted over a medium that may be beyond the control and jurisdiction of Docsaab <b>and its suppliers. Accordingly, Docsaab assumes no liability for or relating to the delay, failure, interruption, or corruption of any data or other information transmitted in connection with use of the Docsaab  Services.<br>" +
            "<br>" +
            "The Docsaab Services and the Content are provided on an \"as is\" basis. Docsaab AND ITS SUPPLIERS, TO THE FULLEST EXTENT PERMITTED BY APPLICABLE LAW, DISCLAIM ALL WARRANTIES, EITHER EXPRESS OR IMPLIED, STATUTORY OR OTHERWISE, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT OF THIRD PARTY RIGHTS, AND FITNESS FOR PARTICULAR PURPOSE</b>. Without limiting the foregoing, Docsaab, its licensors, and its suppliers make no representations or warranties about the following:<br>" +
            "<br>" +
            "  1. The accuracy, reliability, completeness, currentness, or timeliness of the Content, software, text, graphics, links, or communications provided on or through the use of the Docsaab Services or Docsaab.<br>" +
            "  2. The satisfaction of any government regulations requiring disclosure of information on prescription drug products or the approval or compliance of any software tools with regard to the Content contained on the Docsaab Services. Incidental and consequential damages, personal injury/wrongful death, lost profits, or damages resulting from lost data or business interruption) resulting from the use of or inability to use the Docsaab Services or the Content, whether based on warranty, contract, tort, or any other legal theory, and whether or not Docsaab is advised of the possibility of such damages. Docsaab is not liable for any personal injury, including death, caused by your use or misuse of the Services, Content, or Public Areas (as defined below). Any claims arising in connection with your use of the Services, any Content, or the Public Areas must be brought within one (1) year of the date of the event giving rise to such action occurred. Remedies under these Terms of Use are exclusive and are limited to those expressly provided for in these Terms of Use.<br>" +
            "<br>" +
            "ADVERTISEMENTS, SEARCHES AND LINKS TO OTHER SITES<br>" +
            "<br>" +
            " Docsaab may select certain sites as priority responses to search terms you enter and Docsaab may agree to allow advertisers to respond to certain search terms with advertisements or sponsored content. Docsaab does not recommend and does not endorse the content on any third -party websites. Docsaab is not responsible for the content of linked third-party sites, sites framed within the Docsaab Services, third-party sites provided as search results, or third-party advertisements, and does not make any representations regarding their content or accuracy. Your use of third-party websites is at your own risk and subject to the Terms of Use for such sites. Docsaab does not endorse any product advertised on the Docsaab Services.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_about_us_page);
        simulateDayNight(/* DAY */ 0);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.docsaab_ic)
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("assignmentsolutions321@gmail.com")
                .addWebsite("http://assignmentsolutions.in/")
                .addFacebook("https://www.facebook.com/assignmentsolutionss/")
                .addTwitter("assignmentsols")
                .addInstagram("assignmentsolutions.in")
                .addItem(getCopyRightsElement())
                .create();
        TextView txtDesc = (TextView)aboutPage.findViewById(R.id.description);
        txtDesc.setText(Html.fromHtml(DESC));
        txtDesc.setGravity(Gravity.LEFT);

        setContentView(aboutPage);
    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_app_docsaab);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(R.color.app_theme_color);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUsPageActivity.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    void simulateDayNight(int currentSetting) {
        final int DAY = 0;
        final int NIGHT = 1;
        final int FOLLOW_SYSTEM = 3;

        int currentNightMode = getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        if (currentSetting == DAY && currentNightMode != Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == NIGHT && currentNightMode != Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == FOLLOW_SYSTEM) {
            AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }
}

/*
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.ideashower.readitlater.pro")
 */